package expansioncontent.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.CardPowerTip;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import expansioncontent.cards.StudyTheSpire;
import expansioncontent.expansionContentMod;
import downfall.util.TextureLoader;

import static expansioncontent.expansionContentMod.makeRelicOutlinePath;
import static expansioncontent.expansionContentMod.makeRelicPath;

public class StudyCardRelic extends CustomRelic {

    public static final String ID = expansionContentMod.makeID("StudyCardRelic");
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("tinybowlerhat.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("tinybowlerhatOutline.png"));

    public StudyCardRelic() {
        super(ID, IMG, OUTLINE, RelicTier.DEPRECATED, LandingSound.FLAT);
        this.tips.add(new CardPowerTip(new StudyTheSpire()));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
//        if (AbstractDungeon.player instanceof SlimeboundCharacter)
//            AbstractDungeon.actionManager.addToBottom(new VFXAction(new TinyHatParticle(AbstractDungeon.player)));
        this.flash();

        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        AbstractCard c;
        c = CardLibrary.getCard(StudyTheSpire.ID).makeCopy();

        c.modifyCostForCombat(-9);
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c));
    }


    @Override
    public AbstractRelic makeCopy() {
        return new StudyCardRelic();
    }

}