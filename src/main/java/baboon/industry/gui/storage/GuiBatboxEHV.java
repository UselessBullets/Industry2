package baboon.industry.gui.storage;

import baboon.industry.block.storage.entity.TileEntityBatboxBase;
import net.minecraft.core.player.inventory.InventoryPlayer;

public class GuiBatboxEHV extends GuiBatboxBase {

    public GuiBatboxEHV(InventoryPlayer inventory, TileEntityBatboxBase tileEntity) {
        super(inventory, tileEntity);
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        int scrnX = xSize / 2;
        drawStringCenteredNoShadow(fontRenderer, "EHV Batbox", scrnX, 6, 4210752);
        fontRenderer.drawString("Inventory", 8, (ySize - 96) + 2, 4210752);
    }
}
