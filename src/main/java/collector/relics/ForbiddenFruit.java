package collector.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.CardPowerTip;
import collector.CollectorCollection;
import collector.CollectorMod;
import collector.cards.collectibles.LuckyWick;
import collector.patches.CollectorBottleField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import downfall.util.TextureLoader;

import java.util.stream.Collectors;

public class ForbiddenFruit extends CustomRelic {
    public static final String ID = CollectorMod.makeID(ForbiddenFruit.class.getSimpleName());
    private static final String IMG_PATH = ForbiddenFruit.class.getSimpleName() + ".png";
    private static final String OUTLINE_IMG_PATH = ForbiddenFruit.class.getSimpleName() + ".png";

    private int stage = 2;
    private boolean selected = true;

    public ForbiddenFruit() {
        super(ID, TextureLoader.getTexture(CollectorMod.makeRelicPath(IMG_PATH)), TextureLoader.getTexture(CollectorMod.makeRelicOutlinePath(OUTLINE_IMG_PATH)), RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void onEquip() {
        stage = 0;
        selected = false;

        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.group.addAll(AbstractDungeon.srcCommonCardPool.group.stream().map(AbstractCard::makeCopy).collect(Collectors.toList()));
        AbstractDungeon.gridSelectScreen.open(group, 1, DESCRIPTIONS[0], false, false, false, false);
    }

    @Override
    public void update() {
        super.update();

        if (!selected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            selected = true;
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            if (stage == 2) {
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                AbstractDungeon.player.increaseMaxHp(8, true);
            }
            else if (stage == 0) {
                selected = false;
                CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                group.group.addAll(AbstractDungeon.srcUncommonCardPool.group.stream().map(AbstractCard::makeCopy).collect(Collectors.toList()));
                AbstractDungeon.gridSelectScreen.open(group, 1, DESCRIPTIONS[0], false, false, false, false);
                stage++;
            }
            else if (stage == 1) {
                selected = false;
                CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                group.group.addAll(AbstractDungeon.srcRareCardPool.group.stream().map(AbstractCard::makeCopy).collect(Collectors.toList()));
                AbstractDungeon.gridSelectScreen.open(group, 1, DESCRIPTIONS[0], false, false, false, false);
                stage++;
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}

