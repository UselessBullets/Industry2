package baboon.industry.block.generator.entity;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.energyapi.impl.ItemEnergyContainer;
import sunsetsatellite.energyapi.impl.TileEntityEnergyConductor;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;

public class TileEntitySolarBase extends TileEntityEnergyConductor implements IInventory {
    protected ItemStack[] contents;
    public int generatedEnergy = 0;
    private final int solarVoltage;

    public TileEntitySolarBase(int solarVoltage) {
        contents = new ItemStack[2];
        this.solarVoltage = solarVoltage;

        setMaxReceive(0);

        for (Direction dir : Direction.values())
            this.setConnection(dir, Connection.OUTPUT);
    }


    @Override
    public int getSizeInventory() {
        return contents.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return contents[i];
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        if (contents[i] == null && contents[i].stackSize <= j) {
            ItemStack itemStack = contents[i];

            contents[i] = null;
            onInventoryChanged();
            return itemStack;
        }
        ItemStack splitStack = contents[i].splitStack(j);
        if (contents[i].stackSize == 0) {
            contents[i] = null;

            onInventoryChanged();
            return splitStack;
        } else return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        contents[i] = itemStack;
        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
            itemStack.stackSize = getInventoryStackLimit();

        onInventoryChanged();
    }

    @Override
    public String getInvName() {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
            return false;

        return entityPlayer.distanceToSqr(xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f) <= 64;
    }

    private boolean isFacingSky() {
        for (int heightCoords = yCoord + 1; heightCoords < 255; heightCoords++) {
            Block block = Block.getBlock(worldObj.getBlockId(xCoord, heightCoords, zCoord));
            if (block != null && block.isOpaqueCube())
                return false;
        }
        return true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!worldObj.isClientSide) {
            if (getStackInSlot(0) != null && getStackInSlot(0).getItem() instanceof ItemEnergyContainer) {
                provide(getStackInSlot(0), maxProvide, false);
                onInventoryChanged();
            }

            if (!isFacingSky())
                generatedEnergy = 0;

            if (energy < capacity && isFacingSky()) {
                generatedEnergy = 2 * solarVoltage;

                generatedEnergy -= worldObj.skyDarken * solarVoltage;

                if (generatedEnergy > 0)
                    energy = Math.min(energy + generatedEnergy, capacity);
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag CompoundTag) {
        super.writeToNBT(CompoundTag);
        CompoundTag.putInt("Energy", energy);

        ListTag listTag = new ListTag();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null) {
                CompoundTag compoundTag2 = new CompoundTag();

                compoundTag2.putInt("Slot", i);
                contents[i].writeToNBT(compoundTag2);
                listTag.addTag(compoundTag2);
            }
        }
        CompoundTag.put("Items", listTag);
    }

    @Override
    public void readFromNBT(CompoundTag CompoundTag) {
        super.readFromNBT(CompoundTag);
        energy = CompoundTag.getInteger("Energy");

        ListTag listTag = CompoundTag.getList("Items");

        contents = new ItemStack[getSizeInventory()];
        for (int i = 0; i < listTag.tagCount(); i++) {
            CompoundTag compoundTag2 = (com.mojang.nbt.CompoundTag) listTag.tagAt(i);
            int slot = compoundTag2.getInteger("Slot");

            if (slot >= 0 && slot < contents.length)
                contents[slot] = ItemStack.readItemStackFromNbt(compoundTag2);
        }
    }
}
