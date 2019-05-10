package ctfbot;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import ctfbot.behavior.*;
import ctfbot.behavior.fire.FireEnemy;
import ctfbot.behavior.focus.FocusDefend;
import ctfbot.behavior.focus.FocusEnemy;
import ctfbot.behavior.focus.FocusPath;
import ctfbot.behavior.move.*;
import ctfbot.map.MapPlaces;
import ctfbot.tc.CTFCommItems;
import ctfbot.tc.CTFCommObjectUpdates;
import ctfbot.tc.msgs.*;
import cz.cuni.amis.pathfinding.alg.astar.AStarResult;
import cz.cuni.amis.pathfinding.map.IPFMapView;
import cz.cuni.amis.pogamut.base.agent.navigation.IPathFuture;
import cz.cuni.amis.pogamut.base.agent.navigation.impl.PrecomputedPathFuture;
import cz.cuni.amis.pogamut.base.communication.command.ICommandListener;
import cz.cuni.amis.pogamut.base.communication.worldview.listener.annotation.EventListener;
import cz.cuni.amis.pogamut.base.communication.worldview.listener.annotation.ObjectClassEventListener;
import cz.cuni.amis.pogamut.base.communication.worldview.object.event.WorldObjectUpdatedEvent;
import cz.cuni.amis.pogamut.base.utils.guice.AgentScoped;
import cz.cuni.amis.pogamut.base.utils.math.DistanceUtils;
import cz.cuni.amis.pogamut.base3d.worldview.object.ILocated;
import cz.cuni.amis.pogamut.base3d.worldview.object.Location;
import cz.cuni.amis.pogamut.base3d.worldview.object.Rotation;
import cz.cuni.amis.pogamut.unreal.communication.messages.UnrealId;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensomotoric.Weapon;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.AgentInfo;
import cz.cuni.amis.pogamut.ut2004.agent.module.sensor.ManualControl;
import cz.cuni.amis.pogamut.ut2004.agent.module.utils.UT2004Skins;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.UT2004PathAutoFixer;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.levelGeometry.RayCastResult;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.navmesh.NavMeshClearanceComputer.ClearanceLimit;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.navmesh.pathfollowing.NavMeshNavigation;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.stuckdetector.AccUT2004DistanceStuckDetector;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.stuckdetector.AccUT2004PositionStuckDetector;
import cz.cuni.amis.pogamut.ut2004.agent.navigation.stuckdetector.AccUT2004TimeStuckDetector;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004Bot;
import cz.cuni.amis.pogamut.ut2004.bot.impl.UT2004BotModuleController;
import cz.cuni.amis.pogamut.ut2004.communication.messages.ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.UT2004ItemType;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Initialize;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbcommands.Jump;
import cz.cuni.amis.pogamut.ut2004.communication.messages.gbinfomessages.*;
import cz.cuni.amis.pogamut.ut2004.teamcomm.bot.UT2004BotTCController;
import cz.cuni.amis.pogamut.ut2004.teamcomm.server.UT2004TCServer;
import cz.cuni.amis.pogamut.ut2004.utils.UT2004BotRunner;
import cz.cuni.amis.utils.ExceptionToString;
import cz.cuni.amis.utils.collections.MyCollections;
import cz.cuni.amis.utils.exception.PogamutException;
import math.geom2d.Vector2D;
import math.geom3d.Vector3D;

import javax.vecmath.Matrix3d;

/**
 * Bot UT class.
 * <p>
 * Author: Patrik Patera
 * Version: 0.0.1
 */
@AgentScoped
public class CTFBot extends UT2004BotTCController<UT2004Bot> {

    private static Object CLASS_MUTEX = new Object();

    public static boolean CAN_USE_NAVIGATE;


    /**
     * TRUE => attempt to auto-load level geometry on bot startup
     */
    public static final boolean LOAD_LEVEL_GEOMETRY = false;

    /**
     * TRUE => draws navmesh and terminates
     */
    public static final boolean DRAW_NAVMESH = true;
    private static boolean navmeshDrawn = false;


    /**
     * Whether to draw navigation path; works only if you are running 1 bot...
     */
    public static final boolean DRAW_NAVIGATION_PATH = true;
    private boolean navigationPathDrawn = false;


    public static final double HEARING_DISTANCE = 1500.0;

    public static final boolean MANUAL_CONTROL = true;

    /**
     * How many bots we have started so far; used to split bots into teams.
     */
    private static AtomicInteger BOT_COUNT = new AtomicInteger(0);


    private CTFCommItems<CTFBot> commItems;
    private CTFCommObjectUpdates<CTFBot> commObjectUpdates;

    //private RoleManager roleManager = new RoleManager();
    private BehaviorManager<CTFBot> behaviorManager;
    private MapPlaces mapPlaces;

    private List<PlayerInfo> myPlayers;
    private PlayerInfo seenEnemyWithOurFlag = null;
    private PlayerInfo seenEnemyFlag = null;
    private PlayerInfo whoIsFlagHolder = null;

    public final static double LOW_HEALTH_RATIO = 0.35;
    public final static double LOW_AMMO_RATIO = 0.5;


    private long lastLogicTime = -1;


    //PARAMS
    private static int team, desiredSkill, teamNum;

    // =============
    // BOT LIFECYCLE
    // =============

    public PlayerInfo getSeenEnemyWithOurFlag() {
        return seenEnemyWithOurFlag;
    }

    /**
     * Bot's preparation - called before the bot is connected to GB2004 and launched into UT2004.
     */
    @Override
    public void prepareBot(UT2004Bot bot) {
        // DEFINE WEAPON PREFERENCES
        initWeaponPreferences();
        // INITIALIZATION OF COMM MODULES
        commItems = new CTFCommItems<CTFBot>(this);
        commObjectUpdates = new CTFCommObjectUpdates<CTFBot>(this);
        behaviorManager = new BehaviorManager<CTFBot>(this);
        myPlayers = new ArrayList<>();
        mapPlaces = null;

        navigation.getPathExecutor().removeAllStuckDetectors();
        navigation.getPathExecutor().addStuckDetector(new AccUT2004TimeStuckDetector(bot, 3000, 5000));
        navigation.getPathExecutor().addStuckDetector(new AccUT2004PositionStuckDetector(bot));
        navigation.getPathExecutor().addStuckDetector(new AccUT2004DistanceStuckDetector(bot));

        // MOVING BEHAVIORS
        behaviorManager.suggestBehavior(new DefendBase(this));
        behaviorManager.suggestBehavior(new StealFlag(this));
        behaviorManager.suggestBehavior(new BackFlag(this));
        behaviorManager.suggestBehavior(new CollectItem(this));
        behaviorManager.suggestBehavior(new GetFlag(this));
        // behaviorManager.suggestBehavior(new HuntEnemy(this));
        behaviorManager.suggestBehavior(new DefendFlager(this));
        //-------------------

        // FOCUSING BEHAVIORS
        behaviorManager.suggestBehavior(new FocusEnemy(this));
        behaviorManager.suggestBehavior(new FocusDefend(this));
        //-------------------

        // FIRING BEHAVIORS
        behaviorManager.suggestBehavior(new FireEnemy(this));
        //-------------------

    }

    public boolean isFarFromBase(Item i) {
        return (i.getLocation().getDistance(info.getLocation()) >= 8000);
    }

    @Override
    protected void initializeModules(UT2004Bot bot) {
        super.initializeModules(bot);
        levelGeometryModule.setAutoLoad(false);
    }

    /**
     * This is a place where you should use map tweaks, i.e., patch original Navigation Graph that comes from UT2004.
     */
    @Override
    public void mapInfoObtained() {
        super.mapInfoObtained();
        // See {@link MapTweaks} for details; add tweaks in there if required.
        MapTweaks.tweak(navBuilder);
        fwMap.refreshPathMatrix();
        navMeshModule.setReloadNavMesh(true);
    }

    /**
     * Define your weapon preferences here (if you are going to use weaponPrefs).
     * <p>
     * For more info, see slides (page 8): http://diana.ms.mff.cuni.cz/pogamut_files/lectures/2010-2011/Pogamut3_Lecture_03.pdf
     */
    private void initWeaponPreferences() {
        weaponPrefs.addGeneralPref(UT2004ItemType.MINIGUN, false);
        weaponPrefs.addGeneralPref(UT2004ItemType.MINIGUN, true);
        weaponPrefs.addGeneralPref(UT2004ItemType.LINK_GUN, true);
        weaponPrefs.addGeneralPref(UT2004ItemType.LIGHTNING_GUN, true);
        weaponPrefs.addGeneralPref(UT2004ItemType.SHOCK_RIFLE, true);
        weaponPrefs.addGeneralPref(UT2004ItemType.ROCKET_LAUNCHER, true);
        weaponPrefs.addGeneralPref(UT2004ItemType.ASSAULT_RIFLE, true);
        weaponPrefs.addGeneralPref(UT2004ItemType.FLAK_CANNON, false);
        weaponPrefs.addGeneralPref(UT2004ItemType.FLAK_CANNON, true);
        weaponPrefs.addGeneralPref(UT2004ItemType.BIO_RIFLE, true);
        weaponPrefs.addGeneralPref(UT2004ItemType.SHIELD_GUN, true);

        weaponPrefs.newPrefsRange(300)
                .add(UT2004ItemType.FLAK_CANNON, true)
                .add(UT2004ItemType.MINIGUN, true)
                .add(UT2004ItemType.LINK_GUN, false)
                .add(UT2004ItemType.SHOCK_RIFLE, true)
                .add(UT2004ItemType.LIGHTNING_GUN, true)
                .add(UT2004ItemType.ASSAULT_RIFLE, true)
                .add(UT2004ItemType.ROCKET_LAUNCHER, true)
                .add(UT2004ItemType.SHIELD_GUN, true);

        weaponPrefs.newPrefsRange(600)
                .add(UT2004ItemType.MINIGUN, true)
                .add(UT2004ItemType.LINK_GUN, true)
                .add(UT2004ItemType.LIGHTNING_GUN, true)
                .add(UT2004ItemType.ROCKET_LAUNCHER, true)
                .add(UT2004ItemType.SHOCK_RIFLE, true)
                .add(UT2004ItemType.FLAK_CANNON, true)
                .add(UT2004ItemType.ASSAULT_RIFLE, true)
                .add(UT2004ItemType.BIO_RIFLE, true);

        weaponPrefs.newPrefsRange(1200)
                .add(UT2004ItemType.MINIGUN, true)
                .add(UT2004ItemType.LIGHTNING_GUN, true)
                .add(UT2004ItemType.LINK_GUN, true)
                .add(UT2004ItemType.ROCKET_LAUNCHER, true)
                .add(UT2004ItemType.SHOCK_RIFLE, true)
                .add(UT2004ItemType.ASSAULT_RIFLE, true)
                .add(UT2004ItemType.FLAK_CANNON, true)
                .add(UT2004ItemType.BIO_RIFLE, true);
        ;

        weaponPrefs.newPrefsRange(2400)
                .add(UT2004ItemType.LIGHTNING_GUN, true)
                .add(UT2004ItemType.MINIGUN, true)
                .add(UT2004ItemType.LINK_GUN, true)
                .add(UT2004ItemType.SHOCK_RIFLE, true)
                .add(UT2004ItemType.ROCKET_LAUNCHER, true)
                .add(UT2004ItemType.ASSAULT_RIFLE, true)
                .add(UT2004ItemType.FLAK_CANNON, true)
                .add(UT2004ItemType.BIO_RIFLE, true);
    }

    @Override
    public Initialize getInitializeCommand() {
        String targetName = "PATERA" + BOT_COUNT.getAndIncrement();

        return new Initialize()
                .setName(targetName)
                .setSkin(UT2004Skins.SKINS[1])
                .setTeam(team).setDesiredSkill(desiredSkill);
    }

    /**
     * Bot has been initialized inside GameBots2004 (Unreal Tournament 2004) and is about to enter the play
     * (it does not have the body materialized yet).
     *
     * @param gameInfo
     * @param currentConfig
     * @param init
     */
    @Override
    public void botInitialized(GameInfo gameInfo, ConfigChange currentConfig, InitedMessage init) {
        // INITIALIZE TABOO SETS, if you have them, HERE

       /* if (MANUAL_CONTROL && (BOT_COUNT_RED_TEAM.get() == 1 && BOT_COUNT_BLUE_TEAM.get() == 0)) {
            log.warning("INITIALIZING MANUAL CONTROL WINDOW");
            manualControl = new ManualControl(bot, info, body, levelGeometryModule, draw, navPointVisibility, navMeshModule);
        }*/

    }

    // ==========================
    // EVENT LISTENERS / HANDLERS
    // ==========================

    /**
     * Checking roles about bots in game. Especially when game started to get captain role!
     *
     * @param msg
     */
    @EventListener(eventClass = TCRoleUpdate.class)
    public void roleUpdate(TCRoleUpdate msg) {
        if (msg == null) return;
        if (myPlayers == null) return;

        boolean add = true;

        for (PlayerInfo p : myPlayers) {
            if (p.getId().equals(msg.getSender())) {
                p.setDistance(msg.getValue());
                add = false;
                break;
            }
        }

        if (add) {
            myPlayers.add(new PlayerInfo(msg.getSender()));
        }

        myPlayers.sort(Comparator.comparingDouble(PlayerInfo::getDistance));

    }

    @EventListener(eventClass = TCFlager.class)
    public void flagerUpdate(TCFlager msg) {
        if (msg == null) return;
        if (msg.player == null) {
            whoIsFlagHolder = null;
            return;
        }

        if (msg.player.getLocation() != null) {
            whoIsFlagHolder = msg.player;
        }
    }

    @EventListener(eventClass = TCEnemyUpdate.class)
    public void enemyUpdate(TCEnemyUpdate msg) {
        if (msg == null) return;

        seenEnemyWithOurFlag = msg.player;
    }

    @EventListener(eventClass = TCEnemyFlagUpdate.class)
    public void enemyFlagUpdate(TCEnemyFlagUpdate msg) {
        if (msg == null) return;

        seenEnemyFlag = msg.player;
    }

    /**
     * {@link PlayerDamaged} listener that senses that "some other bot was hurt".
     *
     * @param event
     */
    @EventListener(eventClass = PlayerDamaged.class)
    public void playerDamaged(PlayerDamaged event) {
        UnrealId botHurtId = event.getId();
        if (botHurtId == null) return;

        int damage = event.getDamage();
        Player botHurt = (Player) world.get(botHurtId); // MAY BE NULL!

        log.info("OTHER HURT: " + damage + " DMG to " + botHurtId.getStringId() + " [type=" + event.getDamageType() + ", weapon=" + event.getWeaponName() + "]");
    }

    /**
     * {@link BotDamaged} listener that senses that "I was hurt".
     *
     * @param event
     */
    @EventListener(eventClass = BotDamaged.class)
    public void botDamaged(BotDamaged event) {
        int damage = event.getDamage();

        if (event.getInstigator() == null) {
            log.info("HURT: " + damage + " DMG done to ME [type=" + event.getDamageType() + ", weapon=" + event.getWeaponName() + "] by UNKNOWN");
        } else {
            UnrealId whoCauseDmgId = event.getInstigator();
            Player player = (Player) world.get(whoCauseDmgId); // MAY BE NULL!

            //TODO test this
            if (player != null) move.turnTo(player);
            else move.turnHorizontal(180);

            log.info("HURT: " + damage + " DMG done to ME [type=" + event.getDamageType()
                    + ", weapon=" + event.getWeaponName() + "] by " + whoCauseDmgId.getStringId());
        }
    }

    /**
     * {@link PlayerKilled} listener that senses that "some other bot has died".
     *
     * @param event
     */
    @EventListener(eventClass = PlayerKilled.class)
    public void playerKilled(PlayerKilled event) {
        UnrealId botDiedId = event.getId();
        if (botDiedId == null) return;

        Player botDied = (Player) world.get(botDiedId);

        if (event.getKiller() == null) {
            log.info("OTHER DIED: " + botDiedId.getStringId() + ", UNKNOWN killer");
        } else {
            UnrealId killerId = event.getKiller();
            if (killerId.equals(info.getId())) {
                log.info("OTHER KILLED: " + botDiedId.getStringId() + " by ME");
            } else {
                Player killer = (Player) world.get(killerId);
                if (botDiedId.equals(killerId)) {
                    log.info("OTHER WAS KILLED: " + botDiedId.getStringId() + " comitted suicide");
                } else {
                    log.info("OTHER WAS KILLED: " + botDiedId.getStringId() + " by " + killerId.getStringId());
                }
            }
        }
    }

    /**
     * {@link BotKilled} listener that senses that "your bot has died".
     */
    @Override
    public void botKilled(BotKilled event) {
        if (event.getKiller() == null) {
            log.info("DEAD");
        } else {
            UnrealId killerId = event.getKiller();
            Player killer = (Player) world.get(killerId);
            log.info("KILLED by" + killerId.getStringId());
        }

        if (info.isShooting()) shoot.stopShooting();
        if (navigation.isNavigating()) navigation.stopNavigation();

        if (whoIsFlagHolder != null && whoIsFlagHolder.getId().equals(info.getId())) {
            tcClient.sendToTeam(new TCFlager(null));
        }

        behaviorManager.cleanUp();
    }

    /**
     * {@link HearNoise} listener that senses that "some noise was heard by the bot".
     *
     * @param event
     */
    @EventListener(eventClass = HearNoise.class)
    public void hearNoise(HearNoise event) {
        if (event == null) return;

        hearFocus(event.getDistance(), event.getRotation().toLocation().getNormalized(), event.getSource());
    }

    @EventListener(eventClass = HearPickup.class)
    public void hearPickup(HearPickup event) {
        if (event == null) return;

        hearFocus(event.getDistance(), event.getRotation().toLocation().getNormalized(), event.getSource());
    }


    /**
     * Hearing enemies and focus on them.
     *
     * @param noiseDistance
     * @param faceLocation
     * @param source
     */
    private void hearFocus(double noiseDistance, Location faceLocation, UnrealId source) {
        if (noiseDistance < 2000) {
            Location botLocation = info.getLocation();
            Location newLoc = botLocation.add(faceLocation.scale(noiseDistance));

            boolean enemy = players.getEnemies().containsKey(source);
            if (enemy && behaviorManager.getPlayerTarget() == null) {
                behaviorManager.suggestBehavior(new FocusPath(this, 999, newLoc));
            }
        }
    }

    /**
     * {@link ItemPickedUp} listener that senses that "your bot has picked up some item".
     * <p>
     * See sources for {@link ItemType} for details about item types / categories / groups.
     *
     * @param event
     */
    @EventListener(eventClass = ItemPickedUp.class)
    public void itemPickedUp(ItemPickedUp event) {
        ItemType itemType = event.getType();
        ItemType.Group itemGroup = itemType.getGroup();
        ItemType.Category itemCategory = itemType.getCategory();

        //   log.info("PICKED " + itemCategory.name + ": " + itemType.getName() + " [group=" + itemGroup.getName() + "]");
    }

    /**
     * {@link IncomingProjectile} listener that senses that "some projectile has appeared OR moved OR disappeared".
     *
     * @param event
     */
    @ObjectClassEventListener(objectClass = IncomingProjectile.class, eventClass = WorldObjectUpdatedEvent.class)
    public void incomingProjectileUpdated(WorldObjectUpdatedEvent<IncomingProjectile> event) {
        IncomingProjectile projectile = event.getObject();
        if (projectile == null) return;
        if (projectile.getLocation() == null) return;
        if (projectile.getType().compareTo(UT2004ItemType.ROCKET_LAUNCHER_PROJECTILE.getName()) != 0) return;

        if (projectile.getLocation().getDistance(info.getLocation()) <= 500 && projectile.getOrigin().getDistance(info.getLocation()) > 300) {

            Vector2D vecDirectionProj = new Vector2D(projectile.getOrigin().x - projectile.getLocation().x,
                    projectile.getOrigin().y - projectile.getLocation().y);

            Vector2D vecDirectionMe = new Vector2D(projectile.getOrigin().x - info.getLocation().x,
                    projectile.getOrigin().y - info.getLocation().y);


            double u = Math.sqrt(Math.pow(vecDirectionMe.getX(), 2) + Math.pow(vecDirectionMe.getY(), 2));
            double v = Math.sqrt(Math.pow(vecDirectionProj.getX(), 2) + Math.pow(vecDirectionProj.getY(), 2));

            double angle = Math.acos((vecDirectionMe.dot(vecDirectionProj)) / (u * v)) * (180 / Math.PI);


            if (angle <= 5) {
                ILocated loc = DistanceUtils.getNearest(navPoints.getNavPoints().values(), info.getLocation());
                if (loc != null) move.dodgeTo(loc, false);
            }
        }

        //TODO TEST Rocket launcher dodging
    }


    /**
     * {@link Player} listener that senses that "some other bot has appeared OR moved OR disappeared"
     * <p>
     * WARNING: this method will also be called during handshaking GB2004.
     *
     * @param event
     */
    @ObjectClassEventListener(objectClass = Player.class, eventClass = WorldObjectUpdatedEvent.class)
    public void playerUpdated(WorldObjectUpdatedEvent<Player> event) {
        if (info.getLocation() == null) {
            // HANDSHAKING GB2004
            return;
        }
        Player player = event.getObject();
        //  log.info("PLAYER UPDATED: " + player.getId().getStringId());
    }

    /**
     * {@link Item} listener that senses that "some SPAWNED item has appeared OR moved OR disappeared"
     *
     * @param event
     */
    @ObjectClassEventListener(objectClass = Item.class, eventClass = WorldObjectUpdatedEvent.class)
    public void itemUpdated(WorldObjectUpdatedEvent<Item> event) {
        if (info.getLocation() == null) {
            // HANDSHAKING GB2004
            return;
        }
    }

    /**
     * {@link FlagInfo} listener that senses changes of CTF game state.
     *
     * @param event
     */
    @ObjectClassEventListener(objectClass = FlagInfo.class, eventClass = WorldObjectUpdatedEvent.class)
    public void flagInfoUpdated(WorldObjectUpdatedEvent<FlagInfo> event) {
        //   log.info("FLAG INFO UPDATED: " + event.getObject());
        if (event.getObject() == null) return;

        FlagInfo flag = event.getObject();
        if (flag.getTeam() == team) {
            //TODO consider
            if (ctf.isOurFlagHome())
                tcClient.sendToTeam(new TCEnemyUpdate(null));

            if (!ctf.isOurFlagHeld() && flag.isVisible())
                tcClient.sendToTeam(new TCEnemyUpdate(new PlayerInfo(flag.getHolder(), flag.getLocation(), 0)));

            if (ctf.isOurFlagDropped() && flag.isVisible())
                tcClient.sendToTeam(new TCEnemyUpdate(new PlayerInfo(null, flag.getLocation(), 0)));
        } else {
            if (flag.getHolder() != null && flag.getHolder().equals(info.getId()))
                tcClient.sendToTeam(new TCFlager(new PlayerInfo(flag.getHolder(), info.getLocation(), 0)));

            if (ctf.isEnemyFlagHome())
                tcClient.sendToTeam(new TCEnemyFlagUpdate(null));

            if (ctf.isEnemyFlagDropped() && flag.isVisible())
                tcClient.sendToTeam(new TCEnemyFlagUpdate(new PlayerInfo(null, flag.getLocation(), 0)));
        }
    }

    /**
     * {@link TeamScore} listener that senses changes within scoring.
     *
     * @param event
     */
    @ObjectClassEventListener(objectClass = TeamScore.class, eventClass = WorldObjectUpdatedEvent.class)
    public void teamScoreUpdated(WorldObjectUpdatedEvent<TeamScore> event) {
        if (event.getObject().getTeam() == team) {
            tcClient.sendToTeam(new TCFlager(null));
        } else {
            // tcClient.sendToTeam(new TCEnemyFlagUpdate(null));
        }
    }

    private long selfLastUpdateStartMillis = 0;
    private long selfTimeDelta = 0;

    /**
     * {@link Self} object has been updated. This update is received about every 50ms. You can use this update
     * to fine-time some of your behavior like "weapon switching". I.e. SELF is updated every 50ms while LOGIC is invoked every 250ms.
     * <p>
     * Note that during "SELF UPDATE" only information about your bot location/rotation ({@link Self}) is updated. All other visibilities
     * remains the same as during last {@link #logic()}.
     * <p>
     * Note that new {@link NavMeshNavigation} is using SELF UPDATES to fine-control the bot's navigation.
     *
     * @param event
     */
    @ObjectClassEventListener(objectClass = Self.class, eventClass = WorldObjectUpdatedEvent.class)
    public void selfUpdated(WorldObjectUpdatedEvent<Self> event) {
        if (lastLogicStartMillis == 0) {
            return;
        }
        if (selfLastUpdateStartMillis == 0) {
            selfLastUpdateStartMillis = System.currentTimeMillis();
            return;
        }
        long selfUpdateStartMillis = System.currentTimeMillis();
        selfTimeDelta = selfUpdateStartMillis - selfLastUpdateStartMillis;
        selfLastUpdateStartMillis = selfUpdateStartMillis;

        try {

        } catch (Exception e) {
            // MAKE SURE THAT YOUR BOT WON'T FAIL!
            log.info(ExceptionToString.process(e));
        } finally {
            //log.info("---[ SELF UPDATE END ]---");
        }

    }

    // ==============
    // MAIN BOT LOGIC
    // ==============

    int targetItemStuck = 0;

    /**
     * Method that is executed only once before the first {@link CTFBot#logic()}
     */
    @Override
    public void beforeFirstLogic() {

        bot.getController().getAct().addCommandListener(Jump.class, o -> {
            ((Jump) o).setForce(0.0).setDoubleJump(false);
        });

        CAN_USE_NAVIGATE = game.getMapName().toLowerCase().compareTo("ctf-citadel") != 0;
        mapPlaces = new MapPlaces(navPoints, game.getMapName(), info.getTeam());

        this.navigation.addStrongNavigationListener(
                changedValue -> {
                    switch (changedValue) {
                        case STUCK:
                            log.info("STUCK!");
                            ++targetItemStuck;
                            behaviorManager.resetMoveAction();
                            break;
                        case STOPPED:
                            log.info("STOPPED!");
                            break;
                        case TARGET_REACHED:
                            log.info("TARGET REACHED!");
                            targetItemStuck = 0;
                            break;
                        case PATH_COMPUTATION_FAILED:
                            log.info("FAILED TO COMPUTE PATH!");
                            break;
                        case NAVIGATING:
                            log.info("Navigation continues...");
                            break;
                    }
                });
    }

    private long lastLogicStartMillis = 0;
    private long lastLogicEndMillis = 0;
    private long timeDelta = 0;

    /**
     * Main method that controls the bot - makes decisions what to do next. It
     * is called iteratively by Pogamut engine every time a synchronous batch
     * from the environment is received. This is usually 4 times per second.
     * <p>
     * This is a typical place from where you start coding your bot. Even though bot
     * can be completely EVENT-DRIVEN, the reactive aproach via "ticking" logic()
     * method is more simple / straight-forward.
     */
    @Override
    public void logic() {
        long logicStartTime = System.currentTimeMillis();

        if (tcClient.isConnected()) {
            askForCaptain(getDistanceAStar(info.getNearestNavPoint(), ctf.getEnemyBase()));
        }

        if (navigation.isNavigating() && navigation.getPathExecutor().getCurrentLink() != null) {
            NavPoint nextNavPoint = navigation.getPathExecutor().getCurrentLink().getToNavPoint();
            if (nextNavPoint != null && nextNavPoint.isTeleporter() && nextNavPoint.getLocation().getDistance(info.getLocation()) <= 250) {
               /* if (CAN_USE_NAVIGATE) navigation.navigate(nextNavPoint);
                else navigateAStarPath(nextNavPoint);*/
                move.dodgeTo(nextNavPoint.getLocation(), false);
            }
        }

        seenAnyFlag();

        //TODO put to separated method + magic numbers!!
        if (MapPlaces.isMapConc()) {
            if (ctf.getOurBase().getLocation().getDistance(info.getLocation()) <= 180 ||
                    navPoints.getNavPoint("CTF-BP2-Concentrate.JumpSpot2").getLocation().getDistance(info.getLocation()) <= 150
                    || ctf.getEnemyBase().getLocation().getDistance(info.getLocation()) <= 180 ||
                    navPoints.getNavPoint("CTF-BP2-Concentrate.JumpSpot11").getLocation().getDistance(info.getLocation()) <= 150) {
                move.setSpeed(0.3);
            } else {
                move.setSpeed(1);
            }
        }

        behaviorManager.execute();


        if (lastLogicStartMillis == 0) {
            lastLogicStartMillis = logicStartTime;
            log.info("===[ LOGIC ITERATION ]===");
            timeDelta = 1;
        } else {
            timeDelta = logicStartTime - lastLogicStartMillis;
            //  log.info("===[ LOGIC ITERATION | Delta: " + (timeDelta) + "ms | Since last: " + (logicStartTime - lastLogicEndMillis) + "ms]===");
            lastLogicStartMillis = logicStartTime;
        }


        try {
            // UPDATE TEAM COMM
            commItems.update();
            commObjectUpdates.update();

            // RANDOM NAVIGATION
            if (navigation.isNavigating()) {
                if (DRAW_NAVIGATION_PATH && navigationPathDrawn) {
                    drawNavigationPath(true);
                    navigationPathDrawn = true;
                }
            } else {
                navigationPathDrawn = false;
            }

            navigationPathDrawn = false;
            //   log.info("RUNNING TO: " + navigation.getCurrentTarget());

        } catch (Exception e) {
            // MAKE SURE THAT YOUR BOT WON'T FAIL!
            log.info(ExceptionToString.process(e));
        } finally {
            // MAKE SURE THAT YOUR LOGIC DOES NOT TAKE MORE THAN 250 MS (Honestly, we have never seen anybody reaching even 150 ms per logic cycle...)
            // Note that it is perfectly OK, for instance, to count all path-distances between you and all possible pickup-points / items in the game
            // sort it and do some inference based on that.
            long timeSpentInLogic = System.currentTimeMillis() - logicStartTime;

            if (timeSpentInLogic >= 245) {
                log.warning("!!! LOGIC TOO DEMANDING !!!");
            }
            // log.info("===[ LOGIC END ]===");
            lastLogicEndMillis = System.currentTimeMillis();
        }
    }


    // ===========
    // MIND LOGGER
    // ===========

    /**
     * It is good in-general to periodically log anything that relates to your's {@link CTFBot#logic()} decision making.
     * <p>
     * You might consider exporting these values to some custom Swing window (you crete for yourself) that will be more readable.
     */
    public void logMind() {
        log.info("My health/armor:   " + info.getHealth() + " / " + info.getArmor() + " (low:" + info.getLowArmor() + " / high:" + info.getHighArmor() + ")");
        log.info("My weapon:         " + weaponry.getCurrentWeapon());
        log.info("Have flag:         " + ctf.isBotCarryingEnemyFlag());
        log.info("Our flag state:    " + ctf.getOurFlag().getState());
        log.info("Enemey flag state: " + ctf.getEnemyFlag().getState());
    }

    // ======================================
    // UT2004 DEATH-MATCH INTERESTING GETTERS
    // ======================================

    /**
     * Returns path-nearest {@link NavPoint} that is covered from 'enemy'. Uses {@link UT2004BotModuleController#getVisibility()}.
     *
     * @param enemy
     * @return
     */
    public NavPoint getNearestCoverPoint(Player enemy) {
        if (!visibility.isInitialized()) {
            log.warning("VISIBILITY NOT INITIALIZED: returning random navpoint");
            return MyCollections.getRandom(navPoints.getNavPoints().values());
        }
        List<NavPoint> coverPoints = new ArrayList<NavPoint>(visibility.getCoverNavPointsFrom(enemy.getLocation()));
        return fwMap.getNearestNavPoint(coverPoints, info.getNearestNavPoint());
    }

    /**
     * Returns whether 'item' is possibly spawned (to your current knowledge).
     *
     * @param item
     * @return
     */
    public boolean isPossiblySpawned(Item item) {
        return items.isPickupSpawned(item);
    }

    /**
     * Returns whether you can actually pick this 'item', based on "isSpawned" and "isPickable" in your current state and knowledge.
     */
    public boolean isCurrentlyPickable(Item item) {
        return isPossiblySpawned(item) && items.isPickable(item);
    }

    // ==========
    // RAYCASTING
    // ==========

    /**
     * Performs a client-side raycast agains UT2004 map geometry.
     * <p>
     * It is not sensible to perform more than 1000 raycasts per logic() per bot.
     *
     * @param from
     * @param to
     * @return
     */
    public RayCastResult raycast(ILocated from, ILocated to) {
        if (!levelGeometryModule.isInitialized())
            return null;
        return levelGeometryModule.getLevelGeometry().rayCast(from.getLocation(), to.getLocation());
    }

    /**
     * Performs a client-side raycast against NavMesh in 'direction'. Returns distance of the edge in given 'direction' sending the ray 'from'.
     *
     * @param from
     * @param direction
     * @return
     */
    public double raycastNavMesh(ILocated from, Vector2D direction) {
        if (!navMeshModule.isInitialized())
            return 0;
        ClearanceLimit limit = navMeshModule.getClearanceComputer().findEdge(from.getLocation(), direction);
        if (limit == null)
            return Double.POSITIVE_INFINITY;
        return from.getLocation().getDistance(limit.getLocation());
    }

    // =======
    // DRAWING
    // =======

    public void drawNavigationPath(boolean clearAll) {
        if (clearAll) {
            draw.clearAll();
        }
        List<ILocated> path = navigation.getCurrentPathCopy();
        for (int i = 1; i < path.size(); ++i) {
            draw.drawLine(path.get(i - 1), path.get(i));
        }
    }

    public void drawPath(IPathFuture<? extends ILocated> pathFuture, boolean clearAll) {
        if (clearAll) {
            draw.clearAll();
        }
        List<? extends ILocated> path = pathFuture.get();
        for (int i = 1; i < path.size(); ++i) {
            draw.drawLine(path.get(i - 1), path.get(i));
        }
    }

    public void drawPath(IPathFuture<? extends ILocated> pathFuture, Color color, boolean clearAll) {
        if (clearAll) {
            draw.clearAll();
        }
        if (color == null) color = Color.WHITE;
        List<? extends ILocated> path = pathFuture.get();
        for (int i = 1; i < path.size(); ++i) {
            draw.drawLine(color, path.get(i - 1), path.get(i));
        }
    }

    // =====
    // AStar
    // =====

    private NavPoint lastAStarTarget = null;
    private NavPoint lastAStarFrom = null;

    public boolean navigateAStarPath(NavPoint targetNavPoint) {
        if (lastAStarTarget == targetNavPoint) {
            if (navigation.isNavigating()) return true;
        }

        PrecomputedPathFuture<ILocated> path = getAStarPath(targetNavPoint);
        if (path == null) {
            navigation.stopNavigation();
            return false;
        }
        lastAStarTarget = targetNavPoint;
        //lastAStarFrom = (NavPoint)path.getPathFrom();
        navigation.navigate(path);

        return true;
    }

    private IPFMapView<NavPoint> mapView = new IPFMapView<NavPoint>() {

        @Override
        public Collection<NavPoint> getExtraNeighbors(NavPoint node, Collection<NavPoint> mapNeighbors) {
            return null;
        }

        @Override
        public int getNodeExtraCost(NavPoint node, int mapCost) {
            return 0;
        }

        @Override
        public int getArcExtraCost(NavPoint nodeFrom, NavPoint nodeTo, int mapCost) {
            return 0;
        }

        @Override
        public boolean isNodeOpened(NavPoint node) {
            return true;
        }

        @Override
        public boolean isArcOpened(NavPoint nodeFrom, NavPoint nodeTo) {
            return true;
        }
    };

    public PrecomputedPathFuture<ILocated> getAStarPath(NavPoint targetNavPoint) {
        NavPoint startNavPoint = info.getNearestNavPoint();
        AStarResult<NavPoint> result = aStar.findPath(startNavPoint, targetNavPoint, mapView);
        if (result == null || !result.isSuccess()) return null;
        PrecomputedPathFuture path = new PrecomputedPathFuture(startNavPoint, targetNavPoint, result.getPath());
        return path;
    }

    // ===========
    // BOOT SYNCHRONIZATION
    // ===========

    public PlayerInfo getSeenEnemyFlag() {
        return seenEnemyFlag;
    }

    private void askForCaptain(double distance) {
        tcClient.sendToTeam(new TCRoleUpdate(info.getId(), distance));
    }

    public PlayerInfo getWhoIsFlagHolder() {
        return whoIsFlagHolder;
    }

    public boolean hasLowAmmo(ItemType itemType) {
        double actual = weaponry.getPrimaryWeaponAmmo(itemType);
        double max = weaponry.getWeaponDescriptor(itemType).getPriMaxAmount();
        return ((actual / max) < LOW_AMMO_RATIO);
    }

    public boolean hasReadyAnyWeapon() {
        Map<ItemType, Weapon> weapons = weaponry.getWeapons();
        if (weapons == null) return false;
        if (weapons.isEmpty()) return false;

        boolean ready = false;
        for (Map.Entry<ItemType, Weapon> i : weapons.entrySet()) {
            if (i.getKey() != UT2004ItemType.TRANSLOCATOR && i.getKey() != UT2004ItemType.SHIELD_GUN) {
                if (weaponry.hasAmmoForWeapon(i.getKey())) return true;

            }
        }

        return false;
    }

    public boolean cantSeeFlagButClose(Location target) {
        boolean flag = target.getDistance(getInfo().getLocation()) <= 300 && !getCTF().getOurFlag().isVisible();
        if (flag) {
            tcClient.sendToTeam(new TCEnemyUpdate(null));
        }
        return flag;
    }

    public boolean isRotatedToEnemy(Player enemy) {
        if (enemy == null) return false;
        double diff = 0;

        Location vecToTarget = enemy.getLocation().sub(info.getLocation()).getNormalized();
        Location rotationVector = info.getRotation().toLocation().getNormalized();
        diff = (vecToTarget.sub(rotationVector)).getLength();

        if (diff > 0.6) {
            return false;
        }

        return true;
    }

    public Location focusWalkEnemy() {
        return mapPlaces.walkFocusEnemy;
    }


    public void seenAnyFlag() {
        if (ctf.getOurFlag().isVisible() && !ctf.isOurFlagHome()) {
            FlagInfo ourFlag = ctf.getOurFlag();
            tcClient.sendToTeam(new TCEnemyUpdate(new PlayerInfo(ourFlag.getHolder(), ourFlag.getLocation(), 0)));
        }

        if (ctf.getEnemyFlag().isVisible() && ctf.isEnemyFlagDropped()) {
            FlagInfo enemyFlag = ctf.getEnemyFlag();
            tcClient.sendToTeam(new TCEnemyFlagUpdate(new PlayerInfo(null, enemyFlag.getLocation(), 0)));
        }
    }

    public Player getEnemyTarget() {
        return behaviorManager.getPlayerTarget();
    }

    public boolean amIAttacker() {
        if (myPlayers == null) return false;
        if (myPlayers.size() <= 2) return false;

        return (!amIDefender() && !amIFlagHolder() && !myPlayers.get(0).getId().equals(info.getId()));
    }

    public double getHealthRatio() {
        return (info.getHealth() / info.game.getFullHealth());
    }


    public Location getFocucDefending() {
        if (mapPlaces == null) return null;
        return mapPlaces.defendingFocus;
    }

    public Location getDefendingPlace() {
        if (mapPlaces == null) return null;
        return mapPlaces.defendingPlace;
    }

    public Location getHidingPlace() {
        if (mapPlaces == null) return null;
        return mapPlaces.hidingPlace;
    }

    public boolean amIFlager() {
        if (myPlayers.isEmpty()) return false;

        return !amIDefender();
    }

    public boolean amIFlagHolder() {
        return ctf.isBotCarryingEnemyFlag();
    }

    public boolean amIDefender() {
        if (myPlayers.isEmpty()) return false;
        if (myPlayers.size() < 2) return false;

        return myPlayers.get(myPlayers.size() - 1).getId().equals(info.getId());
    }

    public double getDistanceAStar(NavPoint from, NavPoint to) {
        IPathFuture<NavPoint> path = getAStar().computePath(from, to);
        if (!path.isDone()) {
            return 1.0D / 0.0;
        } else {
            List<NavPoint> list = path.get();
            if (list == null) return -1;
            if (list.size() == 0) {
                return 0.0D;
            } else {
                double result = 0.0D;
                NavPoint np = (NavPoint) list.get(0);

                for (int i = 1; i < list.size(); ++i) {
                    NavPoint next = (NavPoint) list.get(i);
                    result += np.getLocation().getDistance(next.getLocation());
                    np = next;
                }

                return result;
            }
        }
    }


    // ===========
    // MAIN METHOD
    // ===========

    /**
     * Main execute method of the program.
     *
     * @param args
     * @throws PogamutException
     */
    public static void main(String args[]) throws PogamutException {
        try {
            team = Integer.valueOf(args[1]);
            desiredSkill = Integer.valueOf(args[2]);
            teamNum = Integer.valueOf(args[3]);
            String hostAdd = args[4];

            UT2004TCServer.startTCServer();
            new UT2004BotRunner(CTFBot.class, "PateraBOT").setMain(true).setHost(hostAdd).
                    setLogLevel(Level.INFO).startAgents(teamNum);
        } catch (Exception ex) {
            System.err.println("Error while init bot! Probably bad parameters!");
            ex.printStackTrace();
        }
    }

}
