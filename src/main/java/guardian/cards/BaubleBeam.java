package guardian.cards;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.StarBounceEffect;
import guardian.GuardianMod;
import guardian.patches.AbstractCardEnum;
import sneckomod.SneckoMod;

import static guardian.GuardianMod.makeBetaCardPath;

public class BaubleBeam extends AbstractGuardianCard {
    public static final String ID = GuardianMod.makeID("BaubleBeam");
    public static final String NAME;
    public static final String IMG_PATH = "cards/baubleBeam.png";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardStrings cardStrings;
    private static final int COST = 2;
    private static final int DAMAGE = 10;

    //TUNING CONSTANTS
    private static final int GEMACTIVATIONS = 3;
    private static final int UPGRADE_GEMACTIVATIONS = 1;
    private static final int SOCKETS = 1;
    private static final boolean SOCKETSAREAFTER = true;
    public static String DESCRIPTION;
    public static String UPGRADED_DESCRIPTION;

    //END TUNING CONSTANTS

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADED_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }

    public BaubleBeam() {
        super(ID, NAME, GuardianMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.GUARDIAN, RARITY, TARGET);

        this.baseDamage = DAMAGE;
        this.tags.add(GuardianMod.MULTIHIT);
        this.tags.add(GuardianMod.BEAM);

        //COST MODIFICATION IS ULTRA HACKY... in AbstractG

        //this.sockets.add(GuardianMod.socketTypes.RED);
        this.magicNumber = this.baseMagicNumber = GEMACTIVATIONS;
        this.socketCount = SOCKETS;
        updateDescription();
        loadGemMisc();
        //this.tags.add(SneckoMod.BANNEDFORSNECKO);
        GuardianMod.loadJokeCardImage(this, makeBetaCardPath("BaubleBeam.png"));

    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp) {
        return tmp + calculateBeamDamage();
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, float tmp) {
        return tmp + calculateBeamDamage();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));

        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        for (int i = 0; i < this.damage; i++) {
            final Color gemColor = this.sockets.size() > 0
                    ? this.sockets.get(MathUtils.random(this.sockets.size()-1)).color.cpy()
                    .add(MathUtils.random(-.1f, .1f), MathUtils.random(-.1f, .1f), MathUtils.random(-.1f, .1f), 0f)
                    : new Color(MathUtils.random(0.8F, 1.0F), MathUtils.random(0.6F, 0.8F), MathUtils.random(0.0F, 0.6F), 0.0F);

            AbstractDungeon.actionManager.addToBottom(new VFXAction(new StarBounceEffect(m.hb.cX, m.hb.cY) {{color = gemColor;}}));
        }

        for (int i = 0; i < this.magicNumber; i++) {
            super.useGems(p, m);
        }
    }

    public AbstractCard makeCopy() {
        return new BaubleBeam();
    }

    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            if (this.socketCount < 4) {
                this.socketCount++;
                this.saveGemMisc();
            }

            this.updateDescription();
        }
    }

    public void updateDescription() {

        if (this.socketCount > 0) {
            if (upgraded && UPGRADED_DESCRIPTION != null) {
                this.rawDescription = this.updateGemDescription(UPGRADED_DESCRIPTION, true);
            } else {
                this.rawDescription = this.updateGemDescription(DESCRIPTION, true);
            }
        }
        this.initializeDescription();
    }
}


