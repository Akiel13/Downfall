package guardian.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import downfall.util.TextureLoader;
import guardian.GuardianMod;

public class StasisUpgradeRelic extends CustomRelic {
    public static final String ID = "Guardian:StasisUpgradeRelic";
    public static final String IMG_PATH = "relics/cryoChamber.png";
    public static final String OUTLINE_IMG_PATH = "relics/cryoChamberOutline.png";
    public static final String LARGE_IMG_PATH = "relics/cryoChamberLarge.png";

    public StasisUpgradeRelic() {
        super(ID, new Texture(GuardianMod.getResourcePath(IMG_PATH)), new Texture(GuardianMod.getResourcePath(OUTLINE_IMG_PATH)),
                RelicTier.RARE, LandingSound.FLAT);
        this.largeImg = TextureLoader.getTexture(GuardianMod.getResourcePath(LARGE_IMG_PATH));
    }

    public void onEquip() {
        ++AbstractDungeon.player.masterMaxOrbs;
    }

    public void onUnequip() {
        --AbstractDungeon.player.masterMaxOrbs;
    }


    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new StasisUpgradeRelic();
    }
}