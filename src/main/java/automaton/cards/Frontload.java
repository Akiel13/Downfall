package automaton.cards;

import automaton.AutomatonMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import downfall.cardmods.RetainCardMod;

import static automaton.AutomatonMod.makeBetaCardPath;

public class Frontload extends AbstractBronzeCard {

    public final static String ID = makeID("Frontload");

    //stupid intellij stuff skill, self, common

    private static final int BLOCK = 8;
    private static final int UPG_BLOCK = 3;

    public Frontload() {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = BLOCK;
        thisEncodes();
        AutomatonMod.loadJokeCardImage(this, makeBetaCardPath("Frontload.png"));
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
    }

    @Override
    public void onCompile(AbstractCard function, boolean forGameplay) {
        CardModifierManager.addModifier(function, new RetainCardMod());
    }

    public void upp() {
        upgradeBlock(UPG_BLOCK);
    }
}