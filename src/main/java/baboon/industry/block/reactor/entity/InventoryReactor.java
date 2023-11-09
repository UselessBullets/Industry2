package baboon.industry.block.reactor.entity;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;

import java.util.List;

public class InventoryReactor implements IInventory {
    private final List<IInventory> chamberInventories;
    public InventoryReactor(List<IInventory> chamberInventories){
        this.chamberInventories = chamberInventories;
    }
    @Override
    public int getSizeInventory() {
        int size = 0;
        for (IInventory chamber: chamberInventories) {
            size += chamber.getSizeInventory();
        }
        return size;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        int index = 0;
        int slotsCount = 0;
        for (IInventory chamber: chamberInventories) {
            for (int j = 0; j < chamber.getSizeInventory(); j++) {
                if (i == index){
                    return chamber.getStackInSlot(i - slotsCount);
                }
                index++;
            }
            slotsCount += chamber.getSizeInventory();
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        int index = 0;
        int slotsCount = 0;
        for (IInventory chamber: chamberInventories) {
            for (int k = 0; k < chamber.getSizeInventory(); k++) {
                if (i == index){
                    return chamber.decrStackSize(i - slotsCount, j);
                }
                index++;
            }
            slotsCount += chamber.getSizeInventory();
        }
        return null;
    }
    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        int index = 0;
        int slotsCount = 0;
        for (IInventory chamber: chamberInventories) {
            for (int k = 0; k < chamber.getSizeInventory(); k++) {
                if (i == index){
                    chamber.setInventorySlotContents(i - slotsCount, itemStack);
                    return;
                }
                index++;
            }
            slotsCount += chamber.getSizeInventory();
        }
    }

    @Override
    public String getInvName() {
        return "IndustryReactorInventory";
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void onInventoryChanged() {
        for (IInventory chamber: chamberInventories) {
            chamber.onInventoryChanged();
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        for (IInventory chamber: chamberInventories) {
            if (!chamber.canInteractWith(entityPlayer)){
                return false;
            }
        }
        return true;
    }
}
