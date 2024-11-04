package sneckomod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import expansioncontent.util.SecondDownfallMagic;
import sneckomod.SneckoMod;

public class Amass extends AbstractSneckoCard {

    public static final String ID = SneckoMod.makeID("Amass");
    private static final int COST = 1;
    private static final int BASE_BLOCK = 6;
    private static final int UPGRADE_BLOCK = 2;
    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;


    public Amass() {
        super(ID, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        SneckoMod.loadJokeCardImage(this, "Amass.png");
        baseMagicNumber = magicNumber = MAGIC;
        baseBlock = BASE_BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int newstuff = baseBlock;
        int totalEnergyCost = 0;

        for (AbstractCard card : p.hand.group) {
            totalEnergyCost += card.costForTurn;
        }

        int blockAmount = baseBlock + totalEnergyCost;


        baseBlock = blockAmount;

        blck();
        baseBlock = newstuff;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            upgradeMagicNumber(UPGRADE_MAGIC);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
