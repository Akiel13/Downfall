package downfall;

/*
This package should contain all content additions strictly related to the
Evil Mode alternate gameplay run.  This includes Bosses, Events,
Event Override patches, and other things that only appear during Evil Runs.
 */

import automaton.AutomatonChar;
import automaton.AutomatonMod;
import automaton.EasyInfoDisplayPanel;
import automaton.SuperTip;
import automaton.cardmods.EncodeMod;
import automaton.cards.Defend;
import automaton.cards.Strike;
import automaton.potions.BurnAndBuffPotion;
import automaton.relics.*;
import automaton.util.*;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.Pair;
import basemod.abstracts.CustomUnlockBundle;
import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import basemod.helpers.CardModifierManager;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import champ.ChampChar;
import champ.ChampMod;
import champ.cards.ModFinisher;
import champ.potions.CounterstrikePotion;
import champ.powers.LastStandModPower;
import champ.relics.ChampStancesModRelic;
import champ.util.TechniqueMod;
import charbosses.actions.util.CharBossMonsterGroup;
import charbosses.bosses.AbstractCharBoss;
import charbosses.bosses.Defect.CharBossDefect;
import charbosses.bosses.Hermit.CharBossHermit;
import charbosses.bosses.Ironclad.CharBossIronclad;
import charbosses.bosses.Merchant.CharBossMerchant;
import charbosses.bosses.Merchant.CharBossMerchant;
import charbosses.bosses.Silent.CharBossSilent;
import charbosses.bosses.Watcher.CharBossWatcher;
import collector.CollectorChar;
import collector.CollectorMod;
import collector.potions.TempHPPotion;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import downfall.cards.MajorBeam;
import downfall.cards.curses.Sapped;
import collector.util.CollectibleCardReward;
import collector.util.EssenceReward;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.mod.widepotions.WidePotionsMod;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.blights.VoidEssence;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Pride;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.*;
import com.megacrit.cardcrawl.events.city.*;
import com.megacrit.cardcrawl.events.exordium.*;
import com.megacrit.cardcrawl.events.shrines.FaceTrader;
import com.megacrit.cardcrawl.events.shrines.*;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.GoldenIdol;
import com.megacrit.cardcrawl.relics.MedicalKit;
import com.megacrit.cardcrawl.relics.VelvetChoker;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import downfall.actions.MessageCaller;
import downfall.cards.KnowingSkullWish;
import downfall.cards.curses.*;
import downfall.dailymods.*;
import downfall.events.*;
import downfall.events.shrines_evil.DuplicatorEvil;
import downfall.events.shrines_evil.PurificationShrineEvil;
import downfall.events.shrines_evil.TransmogrifierEvil;
import downfall.events.shrines_evil.UpgradeShrineEvil;
import downfall.monsters.*;
import downfall.monsters.gauntletbosses.*;
import downfall.patches.DailyModeEvilPatch;
import downfall.patches.EvilModeCharacterSelect;
import downfall.patches.RewardItemTypeEnumPatch;
import downfall.patches.ui.campfire.AddBustKeyButtonPatches;
import downfall.patches.ui.topPanel.GoldToSoulPatches;
import downfall.potions.CursedFountainPotion;
import downfall.relics.KnowingSkull;
import downfall.relics.*;
import downfall.util.*;
import expansioncontent.cardmods.PropertiesMod;
import expansioncontent.expansionContentMod;
import expansioncontent.patches.CenterGridCardSelectScreen;
import expansioncontent.potions.BossPotion;
import gremlin.GremlinMod;
import gremlin.cards.Wizardry;
import gremlin.characters.GremlinCharacter;
import gremlin.potions.WizPotion;
import gremlin.relics.WizardHat;
import gremlin.relics.WizardStaff;
import guardian.GuardianMod;
import guardian.cards.ExploitGems;
import guardian.characters.GuardianCharacter;
import guardian.potions.BlockOnCardUsePotion;
import guardian.relics.PickAxe;
import guardian.rewards.GemReward;
import guardian.rewards.GemRewardAllRarities;
import hermit.HermitMod;
import slimebound.SlimeboundMod;
import slimebound.characters.SlimeboundCharacter;
import slimebound.potions.ThreeZeroPotion;
import sneckomod.SneckoMod;
import sneckomod.TheSnecko;
import sneckomod.cards.unknowns.*;
import sneckomod.potions.MuddlingPotion;
import sneckomod.util.ColorfulCardReward;

import sneckomod.util.UpgradedUnknownReward;
import theHexaghost.HexaMod;
import theHexaghost.TheHexaghost;
import theHexaghost.potions.SoulburnPotion;
import theHexaghost.util.SealSealReward;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static downfall.patches.EvilModeCharacterSelect.evilMode;
import static reskinContent.reskinContent.unlockAllReskin;
import static sneckomod.OffclassHelper.getARandomOffclass;

@SpireInitializer
public class downfallMod implements OnPlayerDamagedSubscriber, OnStartBattleSubscriber, PostDrawSubscriber, PostDungeonInitializeSubscriber, EditStringsSubscriber, EditKeywordsSubscriber, AddCustomModeModsSubscriber, PostInitializeSubscriber, EditRelicsSubscriber, EditCardsSubscriber, PostUpdateSubscriber, StartGameSubscriber, StartActSubscriber, AddAudioSubscriber, RenderSubscriber, PostDeathSubscriber {
    public static final String modID = "downfall";

    public static final boolean STEAM_MODE = true;

    public static boolean neowtextoverride = false;


    public static boolean choosingBossRelic = false;
    public static boolean choosingRemoveCard = false;
    public static boolean choosingUpgradeCard = false;
    public static boolean choosingTransformCard = false;
    public static boolean overrideBossDifficulty = false;

    public static boolean playedBossCardThisTurn = false; // TODO: remove this and fix related code (abs expansion card)

    public static boolean replaceMenuColor = true;
    public static boolean tempAscensionHack = false;
    public static int tempAscensionOriginalValue = 0;

    //Config Menu Stuff
    private ModPanel settingsPanel;
    public static Properties configDefault = new Properties();
    public static boolean contentSharing_relics = true;
    public static boolean contentSharing_potions = true;
    public static boolean contentSharing_events = false;
    public static boolean contentSharing_colorlessCards = false;
    public static boolean contentSharing_curses = true;
    public static boolean crossoverCharacters = true;
    public static boolean crossoverModCharacters = true;
    public static boolean unlockEverything = false;
    public static boolean noMusic = false;
    public static boolean normalMapLayout = false;
    public static boolean sneckoNoModCharacters = false;
    public static boolean useIconsForAppliedProperties = false;

    public static ArrayList<AbstractRelic> shareableRelics = new ArrayList<>();
    public static final String PROP_RELIC_SHARING = "contentSharing_relics";
    public static final String PROP_POTION_SHARING = "contentSharing_potions";
    public static final String PROP_EVENT_SHARING = "contentSharing_events";
    public static final String PROP_CARD_SHARING = "contentSharing_colorlessCards";
    public static final String PROP_CURSE_SHARING = "contentSharing_curses";
    public static final String PROP_CHAR_CROSSOVER = "crossover_characters";
    public static final String PROP_MOD_CHAR_CROSSOVER = "crossover_mod_characters";
    public static final String PROP_UNLOCK_ALL = "unlockEverything";
    public static final String PROP_NORMAL_MAP = "normalMapLayout";
    public static final String PROP_SNECKO_MODLESS = "sneckoNoModCharacters";
    public static final String PROP_NO_MUSIC = "disableMusicOverride";
    public static final String PROP_ICONS_FOR_APPLIED_PROPERTIES = "useIconsForAppliedProperties";

    public static String Act1BossFaced = "";
    public static String Act2BossFaced = "";
    public static String Act3BossFaced = "";

    public static boolean[] unseenTutorials = new boolean[]{true, // Hermit

            true, // Guardian 1
            true, // Hexa 2
            true, // Charboss Info 3
            true, // COLLECTOR info. Wow, it's hard to believe how much has gone on since the last tutorial was made 4
            true, // t&t advert 5
            true, // Slime Boss 6
            true, // Champ 7
            true, // Auto 8
            true, // Gremlins 9
            true // Snecko 10
            // true, //act3 boss 11
         // true, //act3 boss 12
         // true //act3 boss 13
    };

    public static Properties tutorialSaves = new Properties();

    public static Map<String, String> keywords_and_proper_names = new HashMap<>();

    @SpireEnum
    public static AbstractCard.CardTags CHARBOSS_ATTACK;
    @SpireEnum
    public static AbstractCard.CardTags CHARBOSS_SETUP;
    @SpireEnum
    public static AbstractCard.CardTags DOWNFALL_CURSE;

    public static final boolean EXPERIMENTAL_FLIP = false;
    public static Settings.GameLanguage[] SupportedLanguages = {
            // Insert other languages here
            // DONT FORGET TO TOGGLE AT reskinContent.getLanguageString() TOO
            Settings.GameLanguage.ENG, Settings.GameLanguage.ZHS,
             Settings.GameLanguage.JPN,
            Settings.GameLanguage.KOR,
       //     Settings.GameLanguage.FRA,
//            Settings.GameLanguage.ZHT,
      //      Settings.GameLanguage.RUS,
        //    Settings.GameLanguage.PTB
    };

    public static ReplaceData[] wordReplacements;
    public static SpireConfig bruhData = null;


    private static ArrayList<AbstractCard> downfallCurses = new ArrayList<>();

    public static CustomMod evilWithinSingleton = null;

    public static Texture soulsImage;

    public downfallMod() {
        BaseMod.subscribe(this);


        configDefault.setProperty(PROP_CURSE_SHARING, "FALSE");
        configDefault.setProperty(PROP_RELIC_SHARING, "TRUE");
        configDefault.setProperty(PROP_EVENT_SHARING, "TRUE");
        configDefault.setProperty(PROP_POTION_SHARING, "TRUE");
        configDefault.setProperty(PROP_CARD_SHARING, "TRUE");
        configDefault.setProperty(PROP_CHAR_CROSSOVER, "FALSE");
        configDefault.setProperty(PROP_NORMAL_MAP, "TRUE");
        configDefault.setProperty(PROP_UNLOCK_ALL, "FALSE");
        configDefault.setProperty(PROP_NO_MUSIC, "FALSE");
        configDefault.setProperty(PROP_ICONS_FOR_APPLIED_PROPERTIES, "FALSE");


        loadConfigData();
    }

    public static void initialize() {
        new downfallMod();

        try {
            for (int i = 0; i < unseenTutorials.length; i++) {
                tutorialSaves.setProperty("activeTutorials" + i, "true");
            }
            SpireConfig config = new SpireConfig("downfall", "TutorialsViewed", tutorialSaves);
            for (int j = 0; j < unseenTutorials.length; j++) {
                unseenTutorials[j] = config.getBool("activeTutorials" + j);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveTutorialsSeen() throws IOException {
        SpireConfig config = new SpireConfig("downfall", "TutorialsViewed");
        int i;
        for (i = 0; i < unseenTutorials.length; i++) {
            config.setBool("activeTutorials" + i, unseenTutorials[i]);
        }
        config.save();
    }

    public static final String makeID(String id) {
        return modID + ":" + id;
    }


    public static String assetPath(String path) {
        return "downfallResources/" + path;
    }

    public static String assetPath(String path, otherPackagePaths otherPath) {
        switch (otherPath) {
            case PACKAGE_GUARDIAN:
                return "guardianResources/" + path;
            case PACKAGE_SLIME:
                return "slimeboundResources/" + path;
            case PACKAGE_SNECKO:
                return "sneckomodResources/" + path;
            case PACKAGE_HEXAGHOST:
                return "hexamodResources/" + path;
            case PACKAGE_EXPANSION:
                return "expansioncontentResources/" + path;
            case PACKAGE_CHAMP:
                return "champResources/" + path;
            case PACKAGE_AUTOMATON:
                return "bronzeResources/" + path;
            case PACKAGE_GREMLIN:
                return "gremlinResources/" + path;
            case PACKAGE_HERMIT:
                return "hermitResources/" + path;
            case PACKAGE_COLLECTOR:
                return "collectorResources/" + path;
        }
        return "downfallResources/" + path;
    }

    public static void saveData() {
        try {
            if (bruhData == null) {
                bruhData = new SpireConfig("downfall", "TrapSaveData");
            }
            SpireConfig config = new SpireConfig("downfall", "downfallSaveData", configDefault);

            config.setBool(PROP_CURSE_SHARING, contentSharing_curses);
            config.setBool(PROP_RELIC_SHARING, contentSharing_relics);
            config.setBool(PROP_EVENT_SHARING, contentSharing_events);
            config.setBool(PROP_POTION_SHARING, contentSharing_potions);
            config.setBool(PROP_CARD_SHARING, contentSharing_colorlessCards);
            config.setBool(PROP_CHAR_CROSSOVER, crossoverCharacters);
            config.setBool(PROP_MOD_CHAR_CROSSOVER, crossoverModCharacters);
            config.setBool(PROP_NORMAL_MAP, normalMapLayout);

            config.setBool(PROP_UNLOCK_ALL, unlockEverything);
            config.setBool(PROP_SNECKO_MODLESS, sneckoNoModCharacters);
            config.setBool(PROP_NO_MUSIC, noMusic);
            config.setBool(PROP_ICONS_FOR_APPLIED_PROPERTIES, useIconsForAppliedProperties);
            config.save();
            GoldenIdol_Evil.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadOtherData() {
        try {
            bruhData = new SpireConfig("downfall", "TrapSaveData");
            GoldenIdol_Evil.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String makeLocalizationPath(Settings.GameLanguage language, String filename) {
        String langPath = getLangString();
        return assetPath("localization/" + langPath + "/" + filename + ".json");
    }

    private String makeLocalizationPath(Settings.GameLanguage language, String filename, otherPackagePaths otherPackage) {
        String langPath = getLangString();
        return assetPath("localization/" + langPath + "/" + filename + ".json", otherPackage);
    }

    private String getLangString() {
        for (Settings.GameLanguage lang : SupportedLanguages) {
            if (lang.equals(Settings.language)) {
                return Settings.language.name().toLowerCase();
            }
        }
        if (Settings.language.equals(Settings.GameLanguage.ZHT)) {
            return "zhs";
        }
        return "eng";
    }

    private void loadLocalization(Settings.GameLanguage language, Class<?> stringType) {
        if (stringType != TutorialStrings.class) {
            //SlimeboundMod.logger.info("loading loc:" + language + "downfall" + stringType);
            BaseMod.loadCustomStringsFile(stringType, makeLocalizationPath(language, stringType.getSimpleName()));

            //SlimeboundMod.logger.info("loading loc:" + language + " PACKAGE_EXPANSION" + stringType);
            BaseMod.loadCustomStringsFile(stringType, makeLocalizationPath(language, stringType.getSimpleName(), otherPackagePaths.PACKAGE_EXPANSION));

            //SlimeboundMod.logger.info("loading loc:" + language + " PACKAGE_GUARDIAN" + stringType);
            BaseMod.loadCustomStringsFile(stringType, makeLocalizationPath(language, stringType.getSimpleName(), otherPackagePaths.PACKAGE_GUARDIAN));

            //SlimeboundMod.logger.info("loading loc:" + language + " PACKAGE_HEXAGHOST" + stringType);
            BaseMod.loadCustomStringsFile(stringType, makeLocalizationPath(language, stringType.getSimpleName(), otherPackagePaths.PACKAGE_HEXAGHOST));

            //SlimeboundMod.logger.info("loading loc:" + language + " PACKAGE_SLIME" + stringType);
            BaseMod.loadCustomStringsFile(stringType, makeLocalizationPath(language, stringType.getSimpleName(), otherPackagePaths.PACKAGE_SLIME));

            //SlimeboundMod.logger.info("loading loc:" + language + " PACKAGE_SNECKO" + stringType);
            BaseMod.loadCustomStringsFile(stringType, makeLocalizationPath(language, stringType.getSimpleName(), otherPackagePaths.PACKAGE_SNECKO));

            //SlimeboundMod.logger.info("loading loc:" + language + " PACKAGE_CHAMP" + stringType);
            BaseMod.loadCustomStringsFile(stringType, makeLocalizationPath(language, stringType.getSimpleName(), otherPackagePaths.PACKAGE_CHAMP));

            //SlimeboundMod.logger.info("loading loc:" + language + " PACKAGE_AUTOMATON" + stringType);
            BaseMod.loadCustomStringsFile(stringType, makeLocalizationPath(language, stringType.getSimpleName(), otherPackagePaths.PACKAGE_AUTOMATON));

            //SlimeboundMod.logger.info("loading loc:" + language + " PACKAGE_GREMLIN" + stringType);
            BaseMod.loadCustomStringsFile(stringType, makeLocalizationPath(language, stringType.getSimpleName(), otherPackagePaths.PACKAGE_GREMLIN));

            //SlimeboundMod.logger.info("loading loc:" + language + " PACKAGE_HERMIT" + stringType);
            BaseMod.loadCustomStringsFile(stringType, makeLocalizationPath(language, stringType.getSimpleName(), otherPackagePaths.PACKAGE_HERMIT));

            BaseMod.loadCustomStringsFile(stringType, makeLocalizationPath(language, stringType.getSimpleName(), otherPackagePaths.PACKAGE_COLLECTOR));
        } else {

            //SlimeboundMod.logger.info("loading loc:" + language + " PACKAGE_HERMIT" + stringType);
            BaseMod.loadCustomStringsFile(stringType, makeLocalizationPath(language, stringType.getSimpleName(), otherPackagePaths.PACKAGE_HERMIT));
        }
    }

    private void loadLocalization(Settings.GameLanguage language) {

        loadLocalization(language, UIStrings.class);
        loadLocalization(language, EventStrings.class);
        loadLocalization(language, RelicStrings.class);
        loadLocalization(language, MonsterStrings.class);
        loadLocalization(language, PotionStrings.class);
        loadLocalization(language, CharacterStrings.class);
        loadLocalization(language, CardStrings.class);
        //loadLocalization(language, KeywordStrings.class);
        loadLocalization(language, OrbStrings.class);
        loadLocalization(language, RunModStrings.class);
        loadLocalization(language, PowerStrings.class);
        loadLocalization(language, RunModStrings.class);
        loadLocalization(language, TutorialStrings.class);
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new KnowingSkullWish());
        // BaseMod.addCard(new Antidote());
        // BaseMod.addCard(new ShieldSmash());
        // BaseMod.addCard(new Debug());
        //BaseMod.addCard(new PeaceOut());
        BaseMod.addCard(new MajorBeam());
        BaseMod.addCard(new Malfunctioning());
        BaseMod.addCard(new Bewildered());
        BaseMod.addCard(new Haunted());
        BaseMod.addCard(new Icky());
        BaseMod.addCard(new Aged());
        BaseMod.addCard(new Pride());
        BaseMod.addCard(new Scatterbrained());
        BaseMod.addCard(new Sapped());
/*
        BaseMod.addCard(new Slug());
        BaseMod.addCard(new Defend_Crowbot());
        BaseMod.addCard(new Boom());
        BaseMod.addCard(new Pellet());
        BaseMod.addCard(new Barrier());
        BaseMod.addCard(new Ricochet());
        BaseMod.addCard(new HeavySlug());
        BaseMod.addCard(new Cannonball());
        BaseMod.addCard(new FullMetalJacket());
        BaseMod.addCard(new Beam());
        BaseMod.addCard(new FanTheHammer());
        BaseMod.addCard(new CompressionMold());
        BaseMod.addCard(new Desperado());*/
    }

    @Override
    public void receiveEditStrings() {
        loadLocalization(Settings.GameLanguage.ENG);
        if (Settings.language != Settings.GameLanguage.ENG) {
            loadLocalization(Settings.language);
        }

        try {
            String lang = getLangString();

            Gson gson = new Gson();
            String json = Gdx.files.internal(assetPath("localization/" + lang + "/replacementStrings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
            wordReplacements = gson.fromJson(json, ReplaceData[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadModKeywords(String modID, otherPackagePaths otherPath) {

        String lang = getLangString();
        //SlimeboundMod.logger.info("loading loc:" + lang + " " + otherPath + " keywords");

        Gson gson = new Gson();
        String json = Gdx.files.internal(assetPath("localization/" + lang + "/KeywordStrings.json", otherPath)).readString(String.valueOf(StandardCharsets.UTF_8));

        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(modID + "", keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                if(!keyword.ID.isEmpty()){ // currently only used by hexa cards, could be expanded to potions too
                    keywords_and_proper_names.put(keyword.ID, keyword.NAMES[0]);
                }
            }
        }
    }

    @Override
    public void receiveEditKeywords() {
        loadModKeywords(HexaMod.getModID(), otherPackagePaths.PACKAGE_HEXAGHOST);
        loadModKeywords(expansionContentMod.getModID(), otherPackagePaths.PACKAGE_EXPANSION);
        loadModKeywords(SneckoMod.getModID(), otherPackagePaths.PACKAGE_SNECKO);
        loadModKeywords(SlimeboundMod.getModID(), otherPackagePaths.PACKAGE_SLIME);
        loadModKeywords(GuardianMod.getModID(), otherPackagePaths.PACKAGE_GUARDIAN);
        loadModKeywords(ChampMod.getModID(), otherPackagePaths.PACKAGE_CHAMP);
        loadModKeywords(AutomatonMod.getModID(), otherPackagePaths.PACKAGE_AUTOMATON);
        loadModKeywords(GremlinMod.getModID(), otherPackagePaths.PACKAGE_GREMLIN);
        loadModKeywords(HermitMod.getModID(), otherPackagePaths.PACKAGE_HERMIT);
        loadModKeywords(CollectorMod.getModID(), otherPackagePaths.PACKAGE_COLLECTOR);
        loadModKeywords(modID, otherPackagePaths.PACKAGE_DOWNFALL);
    }


    public static AbstractCard getRandomDownfallCurse() {
        Collections.shuffle(downfallCurses, AbstractDungeon.cardRandomRng.random);
        return downfallCurses.get(0);
    }

    public static ArrayList<AbstractCard> getRandomDownfallCurse(int count) {
        ArrayList<AbstractCard> ac = new ArrayList<>();
        Collections.shuffle(downfallCurses, AbstractDungeon.cardRandomRng.random);

        for (int i = 0; i < count; i++) {
            ac.add(downfallCurses.get(i));
        }
        return ac;
    }

    public void receivePostInitialize() {
        addPotions();
        UnlockTracker.betaCardPref = new IndividualBetaArtEnablerPref(UnlockTracker.betaCardPref);
        soulsImage = TextureLoader.getTexture(downfallMod.assetPath("images/ui/Souls.png"));

        loadOtherData();

        this.initializeMonsters();
        this.addPotions(); // sorry
        this.initializeEvents();
        this.initializeConfig();


        ArrayList<AbstractCard> tmp = CardLibrary.getAllCards();
        for (AbstractCard c : tmp) {
            if (c.hasTag(DOWNFALL_CURSE)) {
                downfallCurses.add(c);
            }
        }


        //Init save stuff for custom rewards.
        //Automaton
        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.DAZINGPULSE, (rewardSave) -> new DazingPulseReward(), (customReward) -> new RewardSave(customReward.type.toString(), null));

        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.DECABEAM, (rewardSave) -> new DecaBeamReward(), (customReward) -> new RewardSave(customReward.type.toString(), null));

        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.DONUBEAM, (rewardSave) -> new DonuBeamReward(), (customReward) -> new RewardSave(customReward.type.toString(), null));

        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.EXPLODE, (rewardSave) -> new ExplodeReward(), (customReward) -> new RewardSave(customReward.type.toString(), null));

        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.SPIKE, (rewardSave) -> new SpikeReward(), (customReward) -> new RewardSave(customReward.type.toString(), null));

        //Downfall
        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.BOSSCARD, (rewardSave) -> new BossCardReward(), (customReward) -> new RewardSave(customReward.type.toString(), null));

        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.JAXCARD, (rewardSave) -> new JaxReward(), (customReward) -> new RewardSave(customReward.type.toString(), null));

        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.REMOVECARD, (rewardSave) -> new RemoveCardReward(), (customReward) -> new RewardSave(customReward.type.toString(), null));

        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.TRANSFORMCARD, (rewardSave) -> new TransformCardReward(), (customReward) -> new RewardSave(customReward.type.toString(), null));

        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.UPGRADECARD, (rewardSave) -> new UpgradeCardReward(), (customReward) -> new RewardSave(customReward.type.toString(), null));

        //Snecko
        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.COLORFULCARD, (rewardSave) -> new ColorfulCardReward(AbstractCard.CardColor.valueOf(rewardSave.id)), (customReward) -> new RewardSave(customReward.type.toString(), customReward instanceof ColorfulCardReward ? ((ColorfulCardReward) customReward).myColor.toString() : "COLORLESS"));

        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.UPGRADEDUNKNOWNCARD, (rewardSave) -> new UpgradedUnknownReward(), (customReward) -> new RewardSave(customReward.type.toString(), null));

        //Hexaghost
        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.SEALCARD, (rewardSave) -> new SealSealReward(), (customReward) -> new RewardSave(customReward.type.toString(), null));

        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.THIRDSEALCARDREWARD, (rewardSave) -> new ThirdSealReward(), (customReward) -> new RewardSave(customReward.type.toString(), null));

        //Collector
        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.COLLECTOR_COLLECTIBLECARDREWARD, (rewardSave) -> new CollectibleCardReward(rewardSave.id), reward -> {
            String s = ((CollectibleCardReward) reward).card.cardID;
            return new RewardSave(reward.type.toString(), s);
        });

        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.COLLECTOR_ESSENCE, (rewardSave) -> new EssenceReward(rewardSave.amount), (customReward) -> new RewardSave(customReward.type.toString(), null, customReward instanceof EssenceReward ? ((EssenceReward) customReward).amount : 0, 0));

        //Guardian
        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.GEM, (rewardSave) -> { //on load
            GuardianMod.logger.info("gems loaded");
            return new GemReward();
        }, (customReward) -> { //on save
            GuardianMod.logger.info("gems saved");
            return new RewardSave(customReward.type.toString(), null);
        });

        BaseMod.registerCustomReward(RewardItemTypeEnumPatch.GEMALLRARITIES, (rewardSave) -> { //on load
            GuardianMod.logger.info("gems loaded");
            return new GemRewardAllRarities();
        }, (customReward) -> { //on save
            GuardianMod.logger.info("gems saved");
            return new RewardSave(customReward.type.toString(), null);
        });

    }

    private void initializeConfig() {
        UIStrings configStrings = CardCrawlGame.languagePack.getUIString("downfall:ConfigMenuText");

        // Load the Mod Badge
        Texture badgeTexture = new Texture(assetPath("images/badge.png"));
        // Create the Mod Menu

        settingsPanel = new ModPanel();
        int configPos = 750;
        int configStep = 40;

        ModLabeledToggleButton characterCrossoverBtn = new ModLabeledToggleButton(configStrings.TEXT[4], 350.0f, configPos, Settings.CREAM_COLOR, FontHelper.charDescFont, crossoverCharacters, settingsPanel, (label) -> {
        }, (button) -> {
            crossoverCharacters = button.enabled;
            CardCrawlGame.mainMenuScreen.charSelectScreen.options.clear();
            CardCrawlGame.mainMenuScreen.charSelectScreen.initialize();
            saveData();
        });

//        configPos -= configStep;
//        ModLabeledToggleButton useIconsForAppliedCardPropertiesBtn = new ModLabeledToggleButton(configStrings.TEXT[13], 350.0f, configPos, Settings.CREAM_COLOR, FontHelper.charDescFont, useIconsForAppliedProperties, settingsPanel, (label) -> {
//        }, (button) -> {
//            useIconsForAppliedProperties = button.enabled;
//            saveData();
//        });

        settingsPanel.addUIElement(characterCrossoverBtn);
        //settingsPanel.addUIElement(useIconsForAppliedCardPropertiesBtn);

        if (!STEAM_MODE) {
            configPos -= configStep;
            ModLabeledToggleButton characterModCrossoverBtn = new ModLabeledToggleButton(configStrings.TEXT[5], 350.0f, configPos, Settings.CREAM_COLOR, FontHelper.charDescFont, crossoverModCharacters, settingsPanel, (label) -> {
            }, (button) -> {
                crossoverModCharacters = button.enabled;
                CardCrawlGame.mainMenuScreen.charSelectScreen.options.clear();
                CardCrawlGame.mainMenuScreen.charSelectScreen.initialize();
                saveData();
            });

            configPos -= configStep;
            ModLabeledToggleButton contentSharingBtnRelics = new ModLabeledToggleButton(configStrings.TEXT[0], 350.0f, configPos, Settings.CREAM_COLOR, FontHelper.charDescFont, contentSharing_relics, settingsPanel, (label) -> {
            }, (button) -> {
                contentSharing_relics = button.enabled;
                saveData();
            });

            configPos -= configStep;
            ModLabeledToggleButton contentSharingBtnEvents = new ModLabeledToggleButton(configStrings.TEXT[2], 350.0f, configPos, Settings.CREAM_COLOR, FontHelper.charDescFont, contentSharing_events, settingsPanel, (label) -> {
            }, (button) -> {
                contentSharing_events = button.enabled;
                saveData();
            });

            configPos -= configStep;
            ModLabeledToggleButton contentSharingBtnPotions = new ModLabeledToggleButton(configStrings.TEXT[1], 350.0f, configPos, Settings.CREAM_COLOR, FontHelper.charDescFont, contentSharing_potions, settingsPanel, (label) -> {
            }, (button) -> {
                contentSharing_potions = button.enabled;
                saveData();
            });

            configPos -= configStep;
            ModLabeledToggleButton contentSharingBtnColorless = new ModLabeledToggleButton(configStrings.TEXT[3], 350.0f, configPos, Settings.CREAM_COLOR, FontHelper.charDescFont, contentSharing_colorlessCards, settingsPanel, (label) -> {
            }, (button) -> {
                contentSharing_colorlessCards = button.enabled;
                saveData();
            });

            configPos -= configStep;
            ModLabeledToggleButton contentSharingBtnCurses = new ModLabeledToggleButton(configStrings.TEXT[6], 350.0f, configPos, Settings.CREAM_COLOR, FontHelper.charDescFont, contentSharing_curses, settingsPanel, (label) -> {
            }, (button) -> {
                contentSharing_curses = button.enabled;
                saveData();
            });

            configPos -= configStep;
            ModLabeledToggleButton normalMapBtn = new ModLabeledToggleButton(configStrings.TEXT[7], 350.0f, configPos, Settings.CREAM_COLOR, FontHelper.charDescFont, normalMapLayout, settingsPanel, (label) -> {
            }, (button) -> {
                normalMapLayout = button.enabled;
                saveData();
            });

            configPos -= configStep;
            ModLabeledToggleButton sneckoNoModConfig = new ModLabeledToggleButton(configStrings.TEXT[10], 350.0f, configPos, Settings.CREAM_COLOR, FontHelper.charDescFont, sneckoNoModCharacters, settingsPanel, (label) -> {
            }, (button) -> {
                sneckoNoModCharacters = button.enabled;
                saveData();
            });

            configPos -= configStep;
            ModLabeledToggleButton unlockAllBtn = new ModLabeledToggleButton(configStrings.TEXT[8], 350.0f, configPos, Settings.CREAM_COLOR, FontHelper.charDescFont, unlockEverything, settingsPanel, (label) -> {
            }, (button) -> {
                unlockEverything = button.enabled;
                saveData();
            });

            configPos -= configStep;
            ModLabeledToggleButton noMusicBtn = new ModLabeledToggleButton(configStrings.TEXT[11], 350.0f, configPos, Settings.CREAM_COLOR, FontHelper.charDescFont, noMusic, settingsPanel, (label) -> {
            }, (button) -> {
                noMusic = button.enabled;
                saveData();
            });

            configPos -= configStep;
            ModLabeledToggleButton unlockAllSkinBtn = new ModLabeledToggleButton(configStrings.TEXT[12], 350.0f, configPos, Settings.CREAM_COLOR, FontHelper.charDescFont, unlockAllReskin, settingsPanel, (label) -> {
            }, (button) -> {
                unlockAllReskin = button.enabled;
                unlockAllReskin();
            });

            settingsPanel.addUIElement(contentSharingBtnCurses);
            settingsPanel.addUIElement(contentSharingBtnEvents);
            settingsPanel.addUIElement(contentSharingBtnPotions);
            settingsPanel.addUIElement(contentSharingBtnRelics);
            settingsPanel.addUIElement(contentSharingBtnColorless);
            settingsPanel.addUIElement(normalMapBtn);
            settingsPanel.addUIElement(sneckoNoModConfig);
            settingsPanel.addUIElement(unlockAllBtn);
            settingsPanel.addUIElement(noMusicBtn);
            settingsPanel.addUIElement(unlockAllSkinBtn);
            settingsPanel.addUIElement(characterModCrossoverBtn);
        }

        BaseMod.registerModBadge(badgeTexture, "downfall", "Downfall Team", "A very evil Expansion.", settingsPanel);

    }

    public static void loadConfigData() {
        try {
            SpireConfig config = new SpireConfig("downfall", "downfallSaveData", configDefault);
            config.load();
            if (!STEAM_MODE) {
                contentSharing_curses = config.getBool(PROP_CURSE_SHARING);
                contentSharing_relics = config.getBool(PROP_RELIC_SHARING);
                contentSharing_events = config.getBool(PROP_EVENT_SHARING);
                contentSharing_potions = config.getBool(PROP_POTION_SHARING);
                contentSharing_colorlessCards = config.getBool(PROP_CARD_SHARING);
                normalMapLayout = config.getBool(PROP_NORMAL_MAP);
                sneckoNoModCharacters = config.getBool(PROP_SNECKO_MODLESS);
                unlockEverything = config.getBool(PROP_UNLOCK_ALL);
                noMusic = config.getBool(PROP_NO_MUSIC);
            }
            crossoverCharacters = config.getBool(PROP_CHAR_CROSSOVER);
            crossoverModCharacters = config.getBool(PROP_MOD_CHAR_CROSSOVER);
            useIconsForAppliedProperties = config.getBool(PROP_ICONS_FOR_APPLIED_PROPERTIES);
        } catch (Exception e) {
            e.printStackTrace();
            clearData();
        }
    }


    public static void clearData() {
        saveData();
    }

    private void initializeEvents() {
        BaseMod.addEvent(new AddEventParams.Builder(GremlinMatchGame_Evil.ID, GremlinMatchGame_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Prevent from appearing too early//
                .bonusCondition(() -> (AbstractDungeon.floorNum > 6))
                //Event ID to Override//
                .overrideEvent(GremlinMatchGame.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(GremlinWheelGame_Evil.ID, GremlinWheelGame_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Prevent from appearing too early//
                .bonusCondition(() -> (AbstractDungeon.floorNum > 6))
                //Event ID to Override//
                .overrideEvent(GremlinWheelGame.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        //Event only used in Gremlin Wheel relic.  Is not initialized into any Act.
        BaseMod.addEvent(new AddEventParams.Builder(GremlinWheelGame_Rest.ID, GremlinWheelGame_Rest.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> false)
                //Act//
                .dungeonID("").create());

        BaseMod.addEvent(new AddEventParams.Builder(WomanInBlue_Evil.ID, WomanInBlue_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)

                //Prevent from appearing too early//
                .bonusCondition(() -> (AbstractDungeon.floorNum > 6))
                //Event ID to Override//
                .overrideEvent(WomanInBlue.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(LivingWall_Evil.ID, LivingWall_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Prevent from appearing too early//
                .bonusCondition(() -> (AbstractDungeon.floorNum > 6))
                //Event ID to Override//
                .overrideEvent(LivingWall.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(Augmenter_Evil.ID, Augmenter_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Prevent from appearing too early//
                .bonusCondition(() -> (AbstractDungeon.floorNum > 6))
                //Event ID to Override//
                .overrideEvent(DrugDealer.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(BonfireSpirits_Evil.ID, BonfireSpirits_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(Bonfire.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(GoldenShrine_Evil.ID, GoldenShrine_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(GoldShrine.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(FaceTrader_Evil.ID, FaceTrader_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(FaceTrader.ID)

                //Prevent from appearing too early//
                .bonusCondition(() -> AbstractDungeon.floorNum > 6 && (AbstractDungeon.id.equals("TheCity") || AbstractDungeon.id.equals("Exordium")))
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(CursedFountain.ID, CursedFountain.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(FountainOfCurseRemoval.ID)
                //Additional Condition//
                .bonusCondition(() -> AbstractDungeon.player.isCursed())
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(WeMeetAgain_Evil.ID, WeMeetAgain_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(WeMeetAgain.ID)
                //Event Type//
                .bonusCondition(() -> (AbstractDungeon.player.relics.size() > 2))

                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(Designer_Evil.ID, Designer_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(Designer.ID)
                //Event Type//

                .bonusCondition(() -> (AbstractDungeon.id.equals("TheCity") || AbstractDungeon.id.equals("TheBeyond"))).eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(DeadGuy_Evil.ID, DeadGuy_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Prevent from appearing too early//
                .bonusCondition(() -> (AbstractDungeon.floorNum > 6))
                //Event ID to Override//
                .overrideEvent(DeadAdventurer.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(ShiningLight_Evil.ID, ShiningLight_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(ShiningLight.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(WorldOfGoop_Evil.ID, WorldOfGoop_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(GoopPuddle.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(Serpent_Evil.ID, Serpent_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode && !(AbstractDungeon.player instanceof TheSnecko))
                //Event ID to Override//
                .overrideEvent(Sssserpent.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(WingStatue_Evil.ID, WingStatue_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(GoldenWing.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(GoldenIdol_Evil.ID, GoldenIdol_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(GoldenIdol.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(Cleric_Evil.ID, Cleric_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(Cleric.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(CouncilOfGhosts_Evil.ID, CouncilOfGhosts_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(Ghosts.ID)
                //Event Type//

                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(CursedTome_Evil.ID, CursedTome_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(CursedTome.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(ForgottenAltar_Evil.ID, ForgottenAltar_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(ForgottenAltar.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(Bandits_Evil.ID, Bandits_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(MaskedBandits.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(KnowingSkull_Evil.ID, KnowingSkull_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                //Additional Condition//
                .bonusCondition(() -> (AbstractDungeon.player.currentHealth > 12) && AbstractDungeon.id.equals("TheCity")).overrideEvent(com.megacrit.cardcrawl.events.city.KnowingSkull.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(Vagrant_Evil.ID, Vagrant_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(Addict.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(Mausoleum_Evil.ID, Mausoleum_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(TheMausoleum.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(Beggar_Evil.ID, Beggar_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(Beggar.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(TheNest_Evil.ID, TheNest_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(Nest.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(Colosseum_Evil.ID, Colosseum_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode && !(AbstractDungeon.player instanceof ChampChar))
                //Event ID to Override//
                .overrideEvent(Colosseum.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(MindBloom_Evil.ID, MindBloom_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(MindBloom.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(MoaiHead_Evil.ID, MoaiHead_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(MoaiHead.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(Nloth_Evil.ID, Nloth_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(Nloth.ID).bonusCondition(() -> (AbstractDungeon.id.equals("TheCity")))

                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(SensoryStone_Evil.ID, SensoryStone_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(SensoryStone.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(Portal_Evil.ID, Portal_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(SecretPortal.ID)

                .bonusCondition(() -> (AbstractDungeon.id.equals("TheBeyond")))
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(TombRedMask_Evil.ID, TombRedMask_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(TombRedMask.ID)
                //Additional Condition//
                .bonusCondition(() -> AbstractDungeon.player.hasRelic(RedIOU.ID))
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(WindingHalls_Evil.ID, WindingHalls_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(WindingHalls.ID)
                //Event Type//
                .eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(Joust_Evil.ID, Joust_Evil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(TheJoust.ID)
                //Event Type//
                .bonusCondition(() -> (AbstractDungeon.id.equals("TheCity"))).eventType(EventUtils.EventType.FULL_REPLACE).create());

        BaseMod.addEvent(new AddEventParams.Builder(BossTester.ID, BossTester.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> false).create());

        BaseMod.addEvent(new AddEventParams.Builder(DuplicatorEvil.ID, DuplicatorEvil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(Duplicator.ID).eventType(EventUtils.EventType.FULL_REPLACE)
                .bonusCondition(() -> !(AbstractDungeon.player instanceof GuardianCharacter))
                .create());

        BaseMod.addEvent(new AddEventParams.Builder(PurificationShrineEvil.ID, PurificationShrineEvil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(PurificationShrine.ID).eventType(EventUtils.EventType.FULL_REPLACE)
                .bonusCondition(() -> !(AbstractDungeon.player instanceof GuardianCharacter))
                .create());

        BaseMod.addEvent(new AddEventParams.Builder(TransmogrifierEvil.ID, TransmogrifierEvil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(Transmogrifier.ID).eventType(EventUtils.EventType.FULL_REPLACE)
                .bonusCondition(() -> !(AbstractDungeon.player instanceof GuardianCharacter))
                .create());

        BaseMod.addEvent(new AddEventParams.Builder(UpgradeShrineEvil.ID, UpgradeShrineEvil.class) //Event ID//
                //Event Spawn Condition//
                .spawnCondition(() -> evilMode)
                //Event ID to Override//
                .overrideEvent(UpgradeShrine.ID).eventType(EventUtils.EventType.FULL_REPLACE)
                .bonusCondition(() -> !(AbstractDungeon.player instanceof GuardianCharacter))
                .create());
    }

    public static ArrayList<String> possEncounterList = new ArrayList<>();

    private void initializeMonsters() {

        BaseMod.addMonster(LadyInBlue.ID, LadyInBlue::new);

        BaseMod.addMonster(Augmenter.ID, Augmenter::new);

        BaseMod.addMonster(FleeingMerchant.ID, FleeingMerchant::new);

        //BaseMod.addMonster("downfall:CharBossMerchant", () -> new CharBossMonsterGroup(new AbstractMonster[]{new CharBossMerchant()})); moved to boss section

        BaseMod.addMonster(downfall.monsters.FaceTrader.ID, downfall.monsters.FaceTrader::new);

        BaseMod.addMonster("downfall:Heads", LocalizeHelper.DonwfallRunHistoryMonsterNames.TEXT[0], () -> new MonsterGroup(new AbstractMonster[]{new ChangingTotem(), new ForgetfulTotem(), new GrowingTotem(),}));

        BaseMod.addMonster(Augmenter.ID, Augmenter.NAME, () -> new MonsterGroup(new AbstractMonster[]{new Augmenter()}));

        BaseMod.addMonster(LadyInBlue.ID, LadyInBlue.NAME, () -> new MonsterGroup(new AbstractMonster[]{new LadyInBlue()}));

        BaseMod.addMonster(downfall.monsters.FaceTrader.ID, downfall.monsters.FaceTrader.NAME, () -> new MonsterGroup(new AbstractMonster[]{new downfall.monsters.FaceTrader()}));

        BaseMod.addMonster(LooterAlt.ID, LooterAlt.NAME, () -> new MonsterGroup(new AbstractMonster[]{new LooterAlt(0.0F, 0.0F)}));

        BaseMod.addMonster(makeID("LooterAlt2"), LocalizeHelper.RunHistoryMonsterNames.TEXT[6], () -> new MonsterGroup(new AbstractMonster[]{new LooterAlt(-200.0F, 15.0F), new MuggerAlt(80.0F, 0.0F)}));

        float x1 = 200F;
        float x2 = -100F;
        float x3 = -400F;
        float y1 = 0F;
        float y2 = -20F;
        float y3 = 10F;

        BaseMod.addMonster(makeID("Gauntlet1"), "Gauntlet", () -> new MonsterGroup(new AbstractMonster[]{
                //   new Ironclad(),
                //   new Silent(),
                new Defect(x1, y1), new Watcher(x2, y2), new Hermit(x3, y3),}));

        BaseMod.addMonster(makeID("Gauntlet2"), "Gauntlet", () -> new MonsterGroup(new AbstractMonster[]{
                //   new Ironclad(),
                new Silent(x1, y1),
                //   new Defect(),
                new Watcher(x2, y2), new Hermit(x3, y3),}));

        BaseMod.addMonster(makeID("Gauntlet3"), "Gauntlet", () -> new MonsterGroup(new AbstractMonster[]{
                //    new Ironclad(),
                new Silent(x1, y1), new Defect(x2, y2),
                //   new Watcher(),
                new Hermit(x3, y3),}));

        BaseMod.addMonster(makeID("Gauntlet4"), "Gauntlet", () -> new MonsterGroup(new AbstractMonster[]{
                //    new Ironclad(),
                new Silent(x1, y1), new Defect(x2, y2), new Watcher(x3, y3),
                //new Hermit(),
        }));

        BaseMod.addMonster(makeID("Gauntlet5"), "Gauntlet", () -> new MonsterGroup(new AbstractMonster[]{
                //    new Ironclad(),
                new Silent(x1, y1), new Defect(x2, y2), new Watcher(x3, y3),
                //new Hermit(),
        }));

        BaseMod.addMonster(makeID("Gauntlet6"), "Gauntlet", () -> new MonsterGroup(new AbstractMonster[]{new Ironclad(x1, y1),
                //new Silent(),
                //new Defect(),
                new Watcher(x2, y2), new Hermit(x3, y3),}));

        BaseMod.addMonster(makeID("Gauntlet7"), "Gauntlet", () -> new MonsterGroup(new AbstractMonster[]{new Ironclad(x1, y1),
                //new Silent(),
                new Defect(x2, y2), new Watcher(x3, y3),
                //new Hermit(),
        }));

        BaseMod.addMonster(makeID("Gauntlet8"), "Gauntlet", () -> new MonsterGroup(new AbstractMonster[]{new Ironclad(x1, y1),
                //new Silent(),
                new Defect(x2, y2), new Watcher(x3, y3),
                //new Hermit(),
        }));

        BaseMod.addMonster(makeID("Gauntlet9"), "Gauntlet", () -> new MonsterGroup(new AbstractMonster[]{new Ironclad(x1, y1), new Silent(x2, y2),
                //new Defect(),
                //new Watcher(),
                new Hermit(x3, y3),}));

        BaseMod.addMonster(makeID("Gauntlet10"), "Gauntlet", () -> new MonsterGroup(new AbstractMonster[]{new Ironclad(x1, y1), new Silent(x2, y2),
                //new Defect(),
                new Watcher(x3, y3),
                //new Hermit(),
        }));

        BaseMod.addMonster(makeID("Gauntlet11"), "Gauntlet", () -> new MonsterGroup(new AbstractMonster[]{new Ironclad(x1, y1), new Silent(x2, y2), new Defect(x3, y3),
                //new Watcher(),
                //new Hermit(),
        }));


        //RUN HISTORY WORKS NOW!!! You can now actually see what bosses you fought in a run.
        //it doesn't work :(
        BaseMod.addMonster(CharBossIronclad.ID, LocalizeHelper.DonwfallRunHistoryMonsterNames.TEXT[1], () -> new CharBossMonsterGroup(new AbstractMonster[]{new CharBossIronclad()}));
        BaseMod.addMonster(CharBossSilent.ID, LocalizeHelper.DonwfallRunHistoryMonsterNames.TEXT[2], () -> new CharBossMonsterGroup(new AbstractMonster[]{new CharBossSilent()}));
        BaseMod.addMonster(CharBossDefect.ID, LocalizeHelper.DonwfallRunHistoryMonsterNames.TEXT[3], () -> new CharBossMonsterGroup(new AbstractMonster[]{new CharBossDefect()}));
        BaseMod.addMonster(CharBossWatcher.ID, LocalizeHelper.DonwfallRunHistoryMonsterNames.TEXT[4], () -> new CharBossMonsterGroup(new AbstractMonster[]{new CharBossWatcher()}));
        BaseMod.addMonster(CharBossHermit.ID, LocalizeHelper.DonwfallRunHistoryMonsterNames.TEXT[6], () -> new CharBossMonsterGroup(new AbstractMonster[]{new CharBossHermit()}));
        BaseMod.addMonster(CharBossMerchant.ID, LocalizeHelper.DonwfallRunHistoryMonsterNames.TEXT[5], () -> new CharBossMonsterGroup(new AbstractMonster[]{new CharBossMerchant()}));


        BaseMod.addMonster(NeowBoss.ID, () -> new MonsterGroup(new AbstractMonster[]{new NeowBoss()}));
        BaseMod.addMonster(NeowBossFinal.ID, () -> new CharBossMonsterGroup(new AbstractMonster[]{new NeowBossFinal()}));
        BaseMod.addMonster(OldNeow.ID, () -> new MonsterGroup(new AbstractMonster[]{new OldNeow()}));
        BaseMod.addMonster(Neow4Life30Heal.ID, () -> new MonsterGroup(new AbstractMonster[]{new Neow4Life30Heal()}));

    }

    public void addPotions() {
            BaseMod.addPotion(BossPotion.class, Color.MAROON, Color.MAROON, new Color(0x470000ff), BossPotion.POTION_ID);
            BaseMod.addPotion(BlockOnCardUsePotion.class, Color.ROYAL, Color.TEAL, Color.BLUE, BlockOnCardUsePotion.POTION_ID);
            BaseMod.addPotion(MuddlingPotion.class, Color.CYAN, Color.CORAL, Color.MAROON, MuddlingPotion.POTION_ID);
            BaseMod.addPotion(ThreeZeroPotion.class, Color.FOREST, Color.BLACK, Color.BLACK, ThreeZeroPotion.POTION_ID);
           // BaseMod.addPotion(TempHPPotion.class, Color.BLACK, Color.PURPLE, Color.GRAY, TempHPPotion.POTION_ID);
            BaseMod.addPotion(CounterstrikePotion.class, Color.GRAY, Color.GRAY, Color.BLACK, CounterstrikePotion.POTION_ID);
            BaseMod.addPotion(BurnAndBuffPotion.class, Color.RED, Color.GREEN, Color.CLEAR, BurnAndBuffPotion.POTION_ID);
            BaseMod.addPotion(WizPotion.class, Color.PURPLE, Color.PINK, Color.PURPLE, WizPotion.POTION_ID);
        // BaseMod.addPotion(SoulburnPotion.class, Color.GRAY, Color.GRAY, Color.BLACK, SoulburnPotion.POTION_ID, TheHexaghost.Enums.THE_SPIRIT);


        if(EvilModeCharacterSelect.evilMode || downfallMod.contentSharing_events){
            BaseMod.addPotion(CursedFountainPotion.class, Color.PURPLE, Color.MAROON, Color.BLACK, CursedFountainPotion.POTION_ID);
        }

        if (Loader.isModLoaded("widepotions")) {
            WidePotionsMod.whitelistSimplePotion(CursedFountainPotion.POTION_ID);
        }

    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelic(new ShatteredFragment(), RelicType.SHARED);
        BaseMod.addRelic(new BlackCandle(), RelicType.SHARED);
        BaseMod.addRelic(new Hecktoplasm(), RelicType.SHARED);
        BaseMod.addRelic(new BrokenWingStatue(), RelicType.SHARED);
        BaseMod.addRelic(new CloakOfManyFaces(), RelicType.SHARED);
        BaseMod.addRelic(new BurdenOfKnowledge(), RelicType.SHARED);
        BaseMod.addRelic(new GremlinSack(), RelicType.SHARED);
        BaseMod.addRelic(new GremlinWheel(), RelicType.SHARED);
        BaseMod.addRelic(new RedIOU(), RelicType.SHARED);
        BaseMod.addRelic(new RedIOUUpgrade(), RelicType.SHARED);
        BaseMod.addRelic(new KnowingSkull(), RelicType.SHARED);
        BaseMod.addRelic(new HeartBlessingBlue(), RelicType.SHARED);
        BaseMod.addRelic(new HeartBlessingGreen(), RelicType.SHARED);
        BaseMod.addRelic(new HeartBlessingRed(), RelicType.SHARED);
        BaseMod.addRelic(new TeleportStone(), RelicType.SHARED);
        BaseMod.addRelic(new HeartsMalice(), RelicType.SHARED);
        BaseMod.addRelic(new NeowBlessing(), RelicType.SHARED);
        BaseMod.addRelic(new ExtraCursedBell(), RelicType.SHARED);
        BaseMod.addRelic(new ExtraCursedKey(), RelicType.SHARED);
        addPotions();
    }

    public static boolean readyToDoThing = false;

    @Override
    public void receivePostUpdate() {
        if (choosingBossRelic && AbstractDungeon.gridSelectScreen.selectedCards.size() == 1) {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2F, Settings.HEIGHT / 2F, RelicLibrary.getRelic(AbstractDungeon.gridSelectScreen.selectedCards.get(0).cardID));
            choosingBossRelic = false;
            CenterGridCardSelectScreen.centerGridSelect = false;
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        if (choosingUpgradeCard && AbstractDungeon.gridSelectScreen.selectedCards.size() == 1) {
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));// 54
            card.upgrade();
            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));// 59
            choosingUpgradeCard = false;
            CenterGridCardSelectScreen.centerGridSelect = false;
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        if (choosingRemoveCard && AbstractDungeon.gridSelectScreen.selectedCards.size() == 1) {
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            card.untip();// 73
            card.unhover();// 74
            AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, (float) Settings.WIDTH / 2, (float) Settings.HEIGHT / 2.0F));// 75
            AbstractDungeon.player.masterDeck.removeCard(card);// 78
            choosingRemoveCard = false;
            CenterGridCardSelectScreen.centerGridSelect = false;
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        if (choosingTransformCard && AbstractDungeon.gridSelectScreen.selectedCards.size() == 1) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.player.masterDeck.removeCard(c);// 79
            AbstractDungeon.transformCard(c, false, AbstractDungeon.miscRng);// 80
            AbstractCard transCard = AbstractDungeon.getTransformedCard();// 81
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(transCard, c.current_x, c.current_y));// 82
            choosingTransformCard = false;
            CenterGridCardSelectScreen.centerGridSelect = false;
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    public static void resetBossList() {
        possEncounterList.clear();
        possEncounterList.add(CharBossIronclad.ID);
        possEncounterList.add(CharBossSilent.ID);
        possEncounterList.add(CharBossDefect.ID);
        possEncounterList.add(CharBossWatcher.ID);
        possEncounterList.add(CharBossHermit.ID);
    }

    @Override
    public void receiveStartGame() {
        GoldToSoulPatches.changeGoldToSouls(!evilMode);
        if (!CardCrawlGame.loadingSave) {
            AbstractCharBoss.boss = null;
            resetBossList();
            FleeingMerchant.DEAD = false;
            FleeingMerchant.CURRENT_HP = FleeingMerchant.START_HP;
            FleeingMerchant.CURRENT_STRENGTH = 0;
            FleeingMerchant.CURRENT_SOULS = 0;
            // FleeingMerchant.CURRENT_DOOM = 0;
            Cleric_Evil.encountered = false;
            Cleric_Evil.heDead = false;
            GoldToSoulPatches.UpdateMerchantTip();
            AddBustKeyButtonPatches.KeyFields.bustedEmerald.set(AbstractDungeon.player, false);
            AddBustKeyButtonPatches.KeyFields.bustedRuby.set(AbstractDungeon.player, false);
            AddBustKeyButtonPatches.KeyFields.bustedSapphire.set(AbstractDungeon.player, false);

            if ((ModHelper.enabledMods.size() > 0) && ((ModHelper.isModEnabled("The Guardian Cards")) || (ModHelper.isModEnabled("The Slime Boss Cards")))) {
                AbstractDungeon.player.increaseMaxOrbSlots(1, false);
            }
        }
    }

    @Override
    public void receiveStartAct() {
        neowtextoverride = false;
        if (evilMode || (evilWithinSingleton != null && evilWithinSingleton.selected) || (CardCrawlGame.trial == null && DailyModeEvilPatch.todaysRunIsEvil)) {
            if (possEncounterList.size() == 0) {
                resetBossList();
                //SlimeboundMod.logger.info("ERROR! Had to reset the bosses mid-run!");
            }
            if (AbstractDungeon.actNum <= 3) {
                Method setBoss;
                try {
                    AbstractDungeon.bossKey = possEncounterList.remove(AbstractDungeon.cardRandomRng.random(possEncounterList.size() - 1));
                    setBoss = AbstractDungeon.class.getDeclaredMethod("setBoss", String.class);
                    setBoss.setAccessible(true);
                    setBoss.invoke(CardCrawlGame.dungeon, AbstractDungeon.bossKey);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                if (AbstractDungeon.ascensionLevel > 20 && AbstractDungeon.isAscensionMode && AbstractDungeon.actNum == 3) {
                    AbstractDungeon.bossList.add(1, CharBossMerchant.ID);
                }
            }
        }

        if (AbstractDungeon.player != null) {
            if (AbstractDungeon.player.hasRelic(NeowBlessing.ID)) {
                AbstractDungeon.player.increaseMaxHp(100, true);
            }
        }

    }


    public static boolean isDownfallCharacter(AbstractPlayer p) {
        if (p instanceof SlimeboundCharacter || p instanceof TheHexaghost || p instanceof GuardianCharacter || p instanceof TheSnecko || p instanceof ChampChar || p instanceof AutomatonChar || p instanceof GremlinCharacter || p instanceof hermit.characters.hermit || p instanceof CollectorChar) {
            return true;
        }
        return false;
    }


    public void receiveCustomModeMods(List<CustomMod> l) {
        evilWithinSingleton = new CustomMod(EvilRun.ID, "b", false);
        l.add(new CustomMod(WorldOfGoo.ID, "r", true));
        l.add(new CustomMod(Hexed.ID, "r", true));
        l.add(new CustomMod(Jewelcrafting.ID, "g", true));
        l.add(new CustomMod(ChampStances.ID, "g", true));
        l.add(new CustomMod(Enraging.ID, "r", true));
        l.add(new CustomMod(Improvised.ID, "g", true));
        l.add(evilWithinSingleton);
        l.add(new CustomMod(ExchangeController.ID, "r", true));
        l.add(new CustomMod(Analytical.ID, "g", true));
        l.add(new CustomMod(StatusAbuse.ID, "r", true));
        l.add(new CustomMod(TooManyShivs.ID, "r", true));
        l.add(new CustomMod(Wizzardry.ID, "g", true));
        l.add(new CustomMod(TransformRewards.ID, "g", true));
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        if ((CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(WorldOfGoo.ID)) || ModHelper.isModEnabled(WorldOfGoo.ID)) {
            //SlimeboundMod.logger.info("World of goo triggered");
            if (damageInfo.output > AbstractDungeon.player.currentBlock) {

                //SlimeboundMod.logger.info("World of goo succeeded");
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), 1));
            }
        }
        return i;
    }

    @Override
    public void receivePostDungeonInitialize() {
        addPotions();

        if (CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(Jewelcrafting.ID) || ModHelper.isModEnabled(Jewelcrafting.ID)) {
            RelicLibrary.getRelic(PickAxe.ID).makeCopy().instantObtain();
            AbstractDungeon.player.masterDeck.addToTop(new ExploitGems());
            AbstractDungeon.player.masterDeck.addToTop(new ExploitGems());
        }

        if (CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(ChampStances.ID) || ModHelper.isModEnabled(ChampStances.ID)) {
            RelicLibrary.getRelic(ChampStancesModRelic.ID).makeCopy().instantObtain();

            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (!c.hasTag(ChampMod.TECHNIQUE)) CardModifierManager.addModifier(c, new TechniqueMod());
            }
        }

        if ((CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(Hexed.ID)) || ModHelper.isModEnabled(Hexed.ID)) {
            RelicLibrary.getRelic(VelvetChoker.ID).makeCopy().instantObtain();
            AbstractDungeon.bossRelicPool.remove(VelvetChoker.ID);
        }

        if ((CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(ExchangeController.ID)) || ModHelper.isModEnabled(ExchangeController.ID)) {
            RelicLibrary.getRelic(NeowBlessing.ID).makeCopy().instantObtain();

        }

        if ((CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(StatusAbuse.ID)) || ModHelper.isModEnabled(StatusAbuse.ID)) {
            RelicLibrary.getRelic(MakeshiftBattery.ID).makeCopy().instantObtain();
            RelicLibrary.getRelic(MedicalKit.ID).makeCopy().instantObtain();
            RelicLibrary.getRelic(DonusWashers.ID).makeCopy().instantObtain();
            RelicLibrary.getRelic(DecasWashers.ID).makeCopy().instantObtain();
            RelicLibrary.getRelic(BronzeIdol.ID).makeCopy().instantObtain();

            AbstractDungeon.commonRelicPool.clear();
            AbstractDungeon.bossRelicPool.clear();
            AbstractDungeon.uncommonRelicPool.clear();
            AbstractDungeon.rareRelicPool.clear();
            AbstractDungeon.shopRelicPool.clear();

        }

        if ((CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(Analytical.ID)) || ModHelper.isModEnabled(Analytical.ID)) {
            RelicLibrary.getRelic(AnalyticalCore.ID).makeCopy().instantObtain();

            ArrayList<AbstractCard> cardsToRemove = new ArrayList<>();
            ArrayList<AbstractCard> strikes = new ArrayList<>();
            ArrayList<AbstractCard> defends = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
                    AbstractCard c2 = new Strike();
                    if (c.upgraded) c2.upgrade();
                    CardModifierManager.addModifier(c2, new EncodeMod());
                    cardsToRemove.add(c);
                    strikes.add(c2);
                }
                if (c.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) {
                    AbstractCard c2 = new Defend();
                    if (c.upgraded) c2.upgrade();
                    CardModifierManager.addModifier(c2, new EncodeMod());
                    cardsToRemove.add(c);
                    defends.add(c2);
                }
            }
            for (AbstractCard c : cardsToRemove) {
                AbstractDungeon.player.masterDeck.removeCard(c);
            }

            for (AbstractCard c : defends) {
                AbstractDungeon.player.masterDeck.addToBottom(c);
            }
            for (AbstractCard c : strikes) {
                AbstractDungeon.player.masterDeck.addToBottom(c);
            }
        }

        if (CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(Improvised.ID) || ModHelper.isModEnabled(Improvised.ID)) {

            for (int i = 0; i < 10; i++) {
                AbstractCard newCard = getARandomOffclass();
                AbstractDungeon.player.masterDeck.addToTop(newCard);
            }

        }

        if (CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(TooManyShivs.ID) || ModHelper.isModEnabled(TooManyShivs.ID)) {
            RelicLibrary.getRelic(VelvetChoker.ID).makeCopy().instantObtain();
            BlightHelper.getBlight(VoidEssence.ID).instantObtain(AbstractDungeon.player, AbstractDungeon.player.blights.size(), true);
            for (int i = 0; i < 10; i++) {
                AbstractDungeon.player.masterDeck.addToBottom(new Shiv());
            }
        }

        if (CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(Wizzardry.ID) || ModHelper.isModEnabled(Wizzardry.ID)) {
            RelicLibrary.getRelic(WizardHat.ID).makeCopy().instantObtain();
            RelicLibrary.getRelic(WizardStaff.ID).makeCopy().instantObtain();
            AbstractCard c = new Wizardry();
            c.upgrade();
            AbstractDungeon.player.masterDeck.addToBottom(c);

        }

        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            UnlockTracker.markCardAsSeen(c.cardID);

        if ((evilWithinSingleton != null && evilWithinSingleton.selected) || (CardCrawlGame.trial == null && DailyModeEvilPatch.todaysRunIsEvil)) {
            evilMode = true;
        }

        if (AbstractDungeon.player instanceof TheHexaghost) {
            for (AbstractCard c : CardLibrary.getAllCards()) {
                if (c.hasTag(HexaMod.GHOSTWHEELCARD) && c.hasTag(AbstractCard.CardTags.HEALING)) {
                    c.tags.remove(AbstractCard.CardTags.HEALING);
                }
            }
        } else {
            for (AbstractCard c : CardLibrary.getAllCards()) {
                if (c.hasTag(HexaMod.GHOSTWHEELCARD)) {
                    c.tags.add(AbstractCard.CardTags.HEALING);
                }
            }
        }

        Act1BossFaced = "";
        Act2BossFaced = "";
        Act3BossFaced = "";

        playedBossCardThisTurn = false;

        evilWithinSingleton.selected = false;
    }

    public static void saveBossFight(String ID) {
        if (AbstractDungeon.getCurrRoom().event == null && !ID.equals(CharBossMerchant.ID)) {
            switch (AbstractDungeon.actNum) {
                case 1: {
                    Act1BossFaced = ID;
                    break;
                }
                case 2: {
                    Act2BossFaced = ID;
                    break;

                }
                case 3: {
                    Act3BossFaced = ID;
                    break;

                }
                default:
            }
        }
    }


    @Override
    public void receivePostDraw(AbstractCard abstractCard) {
        if ((CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(Hexed.ID)) || ModHelper.isModEnabled(Hexed.ID)) {
            if (!abstractCard.isEthereal) {
                CardModifierManager.addModifier(abstractCard, new PropertiesMod(PropertiesMod.supportedProperties.ETHEREAL, false));
            }
        }
    }

    @Override
    public void receivePostDeath() {
        evilMode = false;
        DailyModeEvilPatch.todaysRunIsEvil = false;
        EasyInfoDisplayPanel.specialDisplays.clear();
        // else: we are doing a quickRestart, do not reset evilMode
    }


    public enum otherPackagePaths {
        PACKAGE_SLIME, PACKAGE_GUARDIAN, PACKAGE_HEXAGHOST, PACKAGE_SNECKO, PACKAGE_EXPANSION, PACKAGE_CHAMP, PACKAGE_AUTOMATON, PACKAGE_GREMLIN, PACKAGE_HERMIT, PACKAGE_COLLECTOR, PACKAGE_DOWNFALL;

        otherPackagePaths() {
        }

    }


    @Override
    public void receiveAddAudio() {
        addAudio(new Pair<>("souls1", "downfallResources/music/souls_rr1.ogg"));
        addAudio(new Pair<>("souls2", "downfallResources/music/souls_rr2.ogg"));
        addAudio(new Pair<>("souls3", "downfallResources/music/souls_rr3.ogg"));
        addAudio(new Pair<>("souls4", "downfallResources/music/souls_rr4.ogg"));
        addAudio(new Pair<>("souls5", "downfallResources/music/souls_rr5.ogg"));
        addAudio(new Pair<>("soulsMain", "downfallResources/music/souls.ogg"));
    }

    private void addAudio(Pair<String, String> audioData) {
        BaseMod.addAudio(audioData.getKey(), audioData.getValue());
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        playedBossCardThisTurn = false;

        if ((CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(ChampStances.ID)) || ModHelper.isModEnabled(ChampStances.ID)) {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new ModFinisher()));
        }

        if ((CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(Enraging.ID)) || ModHelper.isModEnabled(Enraging.ID)) {
            for (AbstractMonster m : abstractRoom.monsters.monsters)
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new LastStandModPower(m, AbstractDungeon.actNum * 2), AbstractDungeon.actNum * 2));
        }

        // Code 0 is Hermit's Tutorial and already within his own mod.

        if ((CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(ChampStances.ID)) || ModHelper.isModEnabled(ChampStances.ID)) {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new ModFinisher()));
        }

        if ((CardCrawlGame.trial != null && CardCrawlGame.trial.dailyModIDs().contains(Enraging.ID)) || ModHelper.isModEnabled(Enraging.ID)) {
            for (AbstractMonster m : abstractRoom.monsters.monsters)
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new LastStandModPower(m, AbstractDungeon.actNum * 2), AbstractDungeon.actNum * 2));
        }

        //guardian tutorial 1
        if (AbstractDungeon.player instanceof GuardianCharacter) {
            if (downfallMod.unseenTutorials[1]) {
                AbstractDungeon.actionManager.addToBottom(new MessageCaller(1));
            }
        }
        //hexa tutorial 2
        if (AbstractDungeon.player instanceof TheHexaghost) {
            if (downfallMod.unseenTutorials[2]) {
                AbstractDungeon.actionManager.addToBottom(new MessageCaller(2));
            }
        }
        //collector tutorial 4
        if (AbstractDungeon.player.chosenClass.equals(CollectorChar.Enums.THE_COLLECTOR)) {
            if (downfallMod.unseenTutorials[4]) {
                AbstractDungeon.actionManager.addToTop(new MessageCaller(4));
            }
        }
        //charboss tutorial 3
        if (abstractRoom instanceof MonsterRoomBoss) {
            if (evilMode) {
                if (downfallMod.unseenTutorials[3]) {
                    AbstractDungeon.actionManager.addToBottom(new MessageCaller(3));
                }
            }
        }
        //slime boss tutorial 6
        if (AbstractDungeon.player instanceof SlimeboundCharacter) {
            if (downfallMod.unseenTutorials[6]) {
                AbstractDungeon.actionManager.addToTop(new MessageCaller(6));
            }
        }
        //champ tutorial 7
        if (AbstractDungeon.player instanceof ChampChar) {
            if (downfallMod.unseenTutorials[7]) {
                AbstractDungeon.actionManager.addToTop(new MessageCaller(7));
            }
        }
        //auto tutorial 8
        if (AbstractDungeon.player instanceof AutomatonChar) {
            if (downfallMod.unseenTutorials[8]) {
                AbstractDungeon.actionManager.addToTop(new MessageCaller(8));
            }
        }
        //grems tutorial 9
        if (AbstractDungeon.player instanceof GremlinCharacter) {
            if (downfallMod.unseenTutorials[9]) {
                AbstractDungeon.actionManager.addToTop(new MessageCaller(9));
            }
        }
        //snecko tutorial 10
        if (AbstractDungeon.player instanceof TheSnecko) {
            if (downfallMod.unseenTutorials[10]) {
                AbstractDungeon.actionManager.addToTop(new MessageCaller(10));
            }
        }
    }


    private static void registerUnlockCardBundle(AbstractPlayer.PlayerClass player, int index, String card1, String card2, String card3) {
        CustomUnlockBundle currentBundle;

        currentBundle = new CustomUnlockBundle(card1, card2, card3);

        UnlockTracker.addCard(card1);
        UnlockTracker.addCard(card2);
        UnlockTracker.addCard(card3);

        BaseMod.addUnlockBundle(currentBundle, player, index);


        if (downfallMod.unlockEverything || UnlockTracker.unlockProgress.getInteger(player.toString() + "UnlockLevel") > index + 1) {

            UnlockTracker.unlockCard(card1);
            UnlockTracker.unlockCard(card2);
            UnlockTracker.unlockCard(card3);
        }
    }


    private static void registerUnlockRelicBundle(AbstractPlayer.PlayerClass player, int index, String relic1, String relic2, String relic3) {
        CustomUnlockBundle currentBundle;

        currentBundle = new CustomUnlockBundle(AbstractUnlock.UnlockType.RELIC, relic1, relic2, relic3);

        UnlockTracker.addRelic(relic1);
        UnlockTracker.addRelic(relic2);
        UnlockTracker.addRelic(relic3);

        BaseMod.addUnlockBundle(currentBundle, player, index);


        if (downfallMod.unlockEverything || UnlockTracker.unlockProgress.getInteger(player.toString() + "UnlockLevel") > index) {

            SlimeboundMod.logger.info("Relic trigger: " + relic1 + " " + UnlockTracker.lockedRelics.contains(relic1) + " " + UnlockTracker.isRelicLocked(relic1) + " " + UnlockTracker.isRelicSeen(relic1));
            SlimeboundMod.logger.info("Relic trigger: " + relic2 + " " + UnlockTracker.lockedRelics.contains(relic2) + " " + UnlockTracker.isRelicLocked(relic2) + " " + UnlockTracker.isRelicSeen(relic3));
            SlimeboundMod.logger.info("Relic trigger: " + relic3 + " " + UnlockTracker.lockedRelics.contains(relic3) + " " + UnlockTracker.isRelicLocked(relic3) + " " + UnlockTracker.isRelicSeen(relic3));
            while (UnlockTracker.lockedRelics.contains(relic1)) {
                UnlockTracker.lockedRelics.remove(relic1);
            }
            while (UnlockTracker.lockedRelics.contains(relic2)) {
                UnlockTracker.lockedRelics.remove(relic2);
            }
            while (UnlockTracker.lockedRelics.contains(relic3)) {
                UnlockTracker.lockedRelics.remove(relic3);
            }
            UnlockTracker.markRelicAsSeen(relic1);
            UnlockTracker.markRelicAsSeen(relic2);
            UnlockTracker.markRelicAsSeen(relic3);
            SlimeboundMod.logger.info("Relic trigger: " + relic1 + " " + UnlockTracker.lockedRelics.contains(relic1) + " " + UnlockTracker.isRelicLocked(relic1) + " " + UnlockTracker.isRelicSeen(relic1));
            SlimeboundMod.logger.info("Relic trigger: " + relic2 + " " + UnlockTracker.lockedRelics.contains(relic2) + " " + UnlockTracker.isRelicLocked(relic2) + " " + UnlockTracker.isRelicSeen(relic3));
            SlimeboundMod.logger.info("Relic trigger: " + relic3 + " " + UnlockTracker.lockedRelics.contains(relic3) + " " + UnlockTracker.isRelicLocked(relic3) + " " + UnlockTracker.isRelicSeen(relic3));
        }
    }


    public static void registerUnlockSuite(String bundle1card1, String bundle1card2, String bundle1card3, String bundle2card1, String bundle2card2, String bundle2card3, String bundle3card1, String bundle3card2, String bundle3card3, String bundle4relic1, String bundle4relic2, String bundle4relic3, String bundle5relic1, String bundle5relic2, String bundle5relic3, AbstractPlayer.PlayerClass player) {

        registerUnlockCardBundle(player, 0, bundle1card1, bundle1card2, bundle1card3);
        registerUnlockCardBundle(player, 1, bundle2card1, bundle2card2, bundle2card3);
        registerUnlockCardBundle(player, 2, bundle3card1, bundle3card2, bundle3card3);
        registerUnlockRelicBundle(player, 3, bundle4relic1, bundle4relic2, bundle4relic3);
        registerUnlockRelicBundle(player, 4, bundle5relic1, bundle5relic2, bundle5relic3);
    }

    public static void registerUnlockSuiteAlternating(String bundle1card1, String bundle1card2, String bundle1card3, String bundle2relic1, String bundle2relic2, String bundle2relic3, String bundle3card1, String bundle3card2, String bundle3card3, String bundle4relic1, String bundle4relic2, String bundle4relic3, String bundle5card1, String bundle5card2, String bundle5card3, AbstractPlayer.PlayerClass player) {

        registerUnlockCardBundle(player, 0, bundle1card1, bundle1card2, bundle1card3);
        registerUnlockRelicBundle(player, 1, bundle2relic1, bundle2relic2, bundle2relic3);
        registerUnlockCardBundle(player, 2, bundle3card1, bundle3card2, bundle3card3);
        registerUnlockRelicBundle(player, 3, bundle4relic1, bundle4relic2, bundle4relic3);
        registerUnlockCardBundle(player, 4, bundle5card1, bundle5card2, bundle5card3);
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        SuperTip.render(sb, EasyInfoDisplayPanel.RENDER_TIMING.TIMING_RENDERSUBSCRIBER);
    }

    public static void removeAnyRelicFromPools(String relicID) {
        if (AbstractDungeon.shopRelicPool.contains(relicID)) AbstractDungeon.shopRelicPool.remove(relicID);
        if (AbstractDungeon.rareRelicPool.contains(relicID)) AbstractDungeon.rareRelicPool.remove(relicID);
        if (AbstractDungeon.uncommonRelicPool.contains(relicID)) AbstractDungeon.uncommonRelicPool.remove(relicID);
        if (AbstractDungeon.bossRelicPool.contains(relicID)) AbstractDungeon.bossRelicPool.remove(relicID);
        if (AbstractDungeon.commonRelicPool.contains(relicID)) AbstractDungeon.commonRelicPool.remove(relicID);
    }
}
