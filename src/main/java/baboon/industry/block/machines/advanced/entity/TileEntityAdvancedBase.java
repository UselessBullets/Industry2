package baboon.industry.block.machines.advanced.entity;

import baboon.industry.IndustryConfig;
import baboon.industry.block.entity.TileEntityEnergyConductorDamageable;
import baboon.industry.recipe.fuel.AdvancedRedstoneFuel;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.helper.Sides;
import sunsetsatellite.energyapi.impl.ItemEnergyContainer;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.IItemIO;

import java.util.Random;

public class TileEntityAdvancedBase extends TileEntityEnergyConductorDamageable implements IInventory, IItemIO {
    protected ItemStack[] contents;
    protected AdvancedRedstoneFuel redstoneFuel = new AdvancedRedstoneFuel();
    public boolean active = false;
    public int redstone = 0;
    public int currentMachineTime = 0;
    public final int maxRedstone = 8000;
    public final int maxMachineTime = 200;

    public TileEntityAdvancedBase() {
        contents = new ItemStack[7];

        setCapacity(IndustryConfig.cfg.getInt("Energy Values.mvStorage"));
        setTransfer(IndustryConfig.cfg.getInt("Energy Values.mediumVoltage"));
        setMaxReceive(IndustryConfig.cfg.getInt("Energy Values.mediumVoltage"));

        for (Direction dir : Direction.values())
            setConnection(dir, Connection.INPUT);
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

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!worldObj.isClientSide) {
            if (getStackInSlot(0) != null && getStackInSlot(0).getItem() instanceof ItemEnergyContainer) {
                provide(getStackInSlot(0), getMaxProvide(), false);
                onInventoryChanged();
            }

            if (getStackInSlot(1) != null && getStackInSlot(1).getItem() instanceof ItemEnergyContainer) {
                receive(getStackInSlot(1), getMaxReceive(), false);
                onInventoryChanged();
            }

            if (contents[6] != null && redstoneFuel.getRedstoneList().containsKey(contents[6].getItem().id)) {
                int newRedstoneLevel = redstoneFuel.getYield(contents[6].getItem().id);

                if (redstoneFuel.getRedstoneFuel(contents[6].getItem().id) != 0 && redstone + newRedstoneLevel <= maxRedstone) {
                    redstone += newRedstoneLevel;
                    --contents[6].stackSize;

                    if (contents[6].stackSize <= 0)
                        contents[6] = null;
                }
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag CompoundTag) {
        super.writeToNBT(CompoundTag);
        CompoundTag.putInt("MachineTime", currentMachineTime);
        CompoundTag.putInt("Energy", energy);
        CompoundTag.putInt("Redstone", redstone);

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
        currentMachineTime = CompoundTag.getInteger("MachineTime");
        energy = CompoundTag.getInteger("Energy");
        redstone = CompoundTag.getInteger("Redstone");

        ListTag listTag = CompoundTag.getList("Items");

        contents = new ItemStack[getSizeInventory()];
        for (int i = 0; i < listTag.tagCount(); i++) {
            CompoundTag compoundTag2 = (com.mojang.nbt.CompoundTag) listTag.tagAt(i);
            int slot = compoundTag2.getInteger("Slot");

            if (slot >= 0 && slot < contents.length)
                contents[slot] = ItemStack.readItemStackFromNbt(compoundTag2);
        }
    }

    @Override
    public int getActiveItemSlotForSide(Direction direction) {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        int index = Sides.orientationLookUpHorizontal[6 * meta + direction.getSide()];
        Random random = new Random();
        direction = Direction.getDirectionFromSide(index);

        if (direction == Direction.Z_POS)
            return 2;
        if (direction == Direction.X_NEG)
            return 3;
        if (direction == Direction.Z_NEG)
            return 4;
        if (direction == Direction.X_POS)
            return 5;
        if (direction == Direction.Y_NEG)
            return 6;

        return -1;
    }

    @Override
    public Connection getItemIOForSide(Direction direction) {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        int index = Sides.orientationLookUpHorizontal[6 * meta + direction.getSide()];
        direction = Direction.getDirectionFromSide(index);

        if (direction == Direction.Z_POS)
            return Connection.INPUT;
        if (direction == Direction.X_NEG)
            return Connection.INPUT;
        if (direction == Direction.Z_NEG)
            return Connection.OUTPUT;
        if (direction == Direction.X_POS)
            return Connection.OUTPUT;
        if (direction == Direction.Y_NEG)
            return Connection.INPUT;

        return Connection.NONE;
    }
}
