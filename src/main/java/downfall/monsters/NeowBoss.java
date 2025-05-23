package downfall.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.HeartMegaDebuffEffect;
import downfall.actions.NeowRezAction;
import downfall.downfallMod;
import guardian.vfx.SmallLaserEffectColored;

import java.util.ArrayList;

public class NeowBoss extends AbstractMonster {

    public static final String ID = downfallMod.makeID("NeowBoss");
    public static final String NAME = CardCrawlGame.languagePack.getCharacterString(ID).NAMES[0];
    private static final float HB_X = -40.0F;
    private static final float HB_Y = -40.0F;
    private static final float HB_W = 700.0F;
    private static final float HB_H = 500.0F;


    private static final float EYE1_X = -130.0F * Settings.scale;
    private static final float EYE1_Y = -50F * Settings.scale;

    private static final float EYE2_X = -20.0F * Settings.scale;
    private static final float EYE2_Y = -50F * Settings.scale;

    private static final float EYE3_X = 80.0F * Settings.scale;
    private static final float EYE3_Y = -50F * Settings.scale;

    private static final float INTENT_X = -210.0F * Settings.scale;
    private static final float INTENT_Y = 280.0F * Settings.scale;

    private static final float DRAWX_OFFSET = 100F * Settings.scale;
    private static final float DRAWY_OFFSET = 30F * Settings.scale;

    private float baseDrawX;

    private int turnNum = 0;

    public boolean isRezzing;
    public boolean offscreen;
    private boolean movingOffscreen;
    private boolean movingBack;

    private float moveTimer;

    public static NeowBoss neowboss;

    private boolean backInTheFight = false;

    public static int Rezzes = 0;

    public ArrayList<String> bossesToRez = new ArrayList<>();
    public ArrayList<String> bossesRezzed = new ArrayList<>();
    private static int StrGain = 3;

    public NeowBoss() {
        super(NAME, ID, 600, HB_X, HB_Y, HB_W, HB_H, "images/npcs/neow/skeleton.png");

        this.loadAnimation("images/npcs/neow/skeleton.atlas", "images/npcs/neow/skeleton.json", 1.0F);

        this.drawX += DRAWX_OFFSET;
        this.drawY += DRAWY_OFFSET;

        type = EnemyType.ELITE;
        this.baseDrawX = drawX;

        StrGain = 1;
        if (AbstractDungeon.ascensionLevel >= 9) {
            StrGain = 2;
        } else {
            setHp(600);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            StrGain = 3;
        }


        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        this.intentOffsetX = INTENT_X;


        // halfDead = true;

        //Initialize the boss list with the four
        Rezzes = 0;

        bossesToRez = NeowBossSelector.returnBossOptions();

    }

    @Override
    public void update() {
        super.update();
        if (movingOffscreen) {
            moveTimer -= Gdx.graphics.getDeltaTime();
            this.drawX = Interpolation.linear.apply(this.baseDrawX + 550F * Settings.scale, this.baseDrawX, moveTimer / 2F);

            if (moveTimer <= 0F) {
                movingOffscreen = false;
                offscreen = true;
            }
        } else if (movingBack) {
            moveTimer -= Gdx.graphics.getDeltaTime();
            this.drawX = Interpolation.linear.apply(this.baseDrawX, this.baseDrawX + 550F * Settings.scale, moveTimer / 2F);

            if (moveTimer <= 0F) {
                movingBack = false;
                offscreen = false;
                // this.halfDead = false;
                //if (!this.hasPower(NeowInvulnerablePower.POWER_ID)) {
                //     AbstractDungeon.getCurrRoom().cannotLose = false;
                // }
                this.drawX = this.baseDrawX;
                //getMove(0);
            }
        }
    }

    public void curses() {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartMegaDebuffEffect()));

        ArrayList<AbstractCard> ac = downfallMod.getRandomDownfallCurse(4);
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(ac.get(0).makeStatEquivalentCopy(), 1, true, false, false, (float) Settings.WIDTH * 0.2F, (float) Settings.HEIGHT / 2.0F));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(ac.get(1).makeStatEquivalentCopy(), 1, true, false, false, (float) Settings.WIDTH * 0.4F, (float) Settings.HEIGHT / 2.0F));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(ac.get(2).makeStatEquivalentCopy(), 1, true, false, false, (float) Settings.WIDTH * 0.6F, (float) Settings.HEIGHT / 2.0F));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(ac.get(3).makeStatEquivalentCopy(), 1, true, false, false, (float) Settings.WIDTH * 0.8F, (float) Settings.HEIGHT / 2.0F));

    }

    @Override
    public void usePreBattleAction() {

        neowboss = this;
        super.usePreBattleAction();
        //  AbstractDungeon.getCurrRoom().cannotLose = true;
        isRezzing = true;
        offscreen = false;
        movingOffscreen = false;
        movingBack = false;
        // halfDead = true;
        // AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_ENDING");


        // AbstractDungeon.getCurrRoom().cannotLose = true;
        playSfx();
        for (int i = 0; i < 5; i++) {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        }

//        curses();

        for (int i = 0; i < 10; i++) {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        }
        AbstractDungeon.actionManager.addToBottom(new NeowRezAction(NeowBoss.neowboss));
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                currentHealth = 0;
                halfDead = true;
            }
        });
    }


    @Override
    public void renderHealth(SpriteBatch sb) {

    }

    public void takeTurn() {
        switch (this.nextMove) {
            case 3: {
                AbstractMonster minion = AbstractDungeon.getRandomMonster(this);
                if (minion != null) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.6F));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffectColored(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX + EYE1_X, this.hb.cY + EYE1_Y, Color.RED), 0.25F));

                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 1, true), 1));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 1, true), 1));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 1, true), 1));
                } else {
                    escape();
                }
            }
            break;
            case 4: {
                playSfx();
                //if(this.hasPower(NeowInvulnerablePower.POWER_ID))  this.halfDead = true;
                AbstractDungeon.actionManager.addToBottom(new NeowRezAction(this));
                break;
            }
            case 5:
                AbstractMonster minion = AbstractDungeon.getRandomMonster(this);
                if (minion != null) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.6F));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffectColored(minion.hb.cX, minion.hb.cY, this.hb.cX + EYE1_X, this.hb.cY + EYE1_Y, Color.GOLD), 0.25F));
                    //AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo) this.damage.get(0), AbstractGameAction.AttackEffect.FIRE, false, true));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(minion, this, new StrengthPower(minion, StrGain), StrGain));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(minion, this, new DexterityPower(minion, StrGain), StrGain));
                    AbstractDungeon.actionManager.addToBottom(new HealAction(minion, this, 30));
                } else {
                    escape();
                    addToBot(new AbstractGameAction() {
                        @Override
                        public void update() {
                            isDone = true;
                            AbstractDungeon.getCurrRoom().endBattle();
                        }
                    });
                }
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void playSfx() {

        int roll = MathUtils.random(3);

        if (roll == 0) {
            CardCrawlGame.sound.play("VO_NEOW_1A");
        } else if (roll == 1) {
            CardCrawlGame.sound.play("VO_NEOW_1B");
        } else if (roll == 2) {
            CardCrawlGame.sound.play("VO_NEOW_2A");
        } else {
            CardCrawlGame.sound.play("VO_NEOW_2B");
        }

    }

    public void switchToRez() {
        if (!isRezzing) {
            this.setMove((byte) 4, Intent.MAGIC);
            isRezzing = true;
            createIntent();
        }
    }


    @Override
    public void render(SpriteBatch sb) {
        flipHorizontal = false;
        super.render(sb);
    }

    public void switchIntentToSelfRez() {
        if (!isEscaping) {
            CardCrawlGame.music.fadeOutBGM();
            CardCrawlGame.music.fadeOutTempBGM();
            escape();
        }
    }

    public void moveForRez() {
        if (!this.offscreen) {
            //this.heal(this.maxHealth);
            movingOffscreen = true;
            moveTimer = 2F;
            //nukeDebuffs();
            //this.isDying = true;
        }
    }

    protected void getMove(int num) {
        if (isRezzing) {
            this.setMove((byte) 4, Intent.MAGIC);
        } else if (turnNum == 0) {
            this.setMove((byte) 5, Intent.BUFF);
            // halfDead = true;
            turnNum = 1;
        } else {
            this.setMove((byte) 3, Intent.DEBUFF);
            // halfDead = true;
            turnNum = 0;
        }
    }
}



