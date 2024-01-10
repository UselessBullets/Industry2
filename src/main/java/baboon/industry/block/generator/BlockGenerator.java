package baboon.industry.block.generator;

import baboon.industry.Industry2;
import baboon.industry.block.generator.entity.TileEntityGenerator;
import net.minecraft.core.block.BlockTileEntityRotatable;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.Catalyst;
import turniplabs.halplibe.helper.TextureHelper;

public class BlockGenerator extends BlockTileEntityRotatable {
    private boolean keepInventory = false;
    private final String MOD_ID = Industry2.MOD_ID;
    private final int[][] machineTexture = new int[][]{
            TextureHelper.getOrCreateBlockTexture(MOD_ID, "generator.png"),
            TextureHelper.getOrCreateBlockTexture(MOD_ID, "generator_on.png"),
            TextureHelper.getOrCreateBlockTexture(MOD_ID, "machine_casing_basic.png")
    };

    public BlockGenerator(String key, int id, Material material) {
        super(key, id, material);
        setupInstance(this);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityGenerator();
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if (!world.isClientSide) {
            TileEntityGenerator tileEntity = (TileEntityGenerator) world.getBlockTileEntity(x, y, z);

            if (tileEntity == null)
                return false;
            Catalyst.displayGui(player, tileEntity, tileEntity.getInvName());
        }
        return true;
    }

    @Override
    public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        /*
        0 = bottom
        1 = top
        2 = north
        3 = south
        4 = west
        5 = east
        */

        TileEntityGenerator tileEntity = ((TileEntityGenerator) blockAccess.getBlockTileEntity(x, y, z));
        int metadata = blockAccess.getBlockMetadata(x, y, z);
        int index = Sides.orientationLookUpHorizontal[6 * metadata + side.getId()];
        if (index != 2)
            atlasIndices[index] = texCoordToIndex(machineTexture[2][0], machineTexture[2][1]);

        if (index == 2) {
            atlasIndices[index] = texCoordToIndex(machineTexture[0][0], machineTexture[0][1]);
            if (tileEntity.active)
                atlasIndices[index] = texCoordToIndex(machineTexture[1][0], machineTexture[1][1]);
        }

        return atlasIndices[index];
    }

    private void dropContents(World world, int x, int y, int z) {
        TileEntityGenerator tileEntity = (TileEntityGenerator) world.getBlockTileEntity(x, y, z);
        if (tileEntity == null)
            System.out.println("Can't drop inventory at X: " + x + " Y: " + y + " Z: " + z + " because TileEntity is null");
        else {
            for (int i = 0; i < tileEntity.getSizeInventory(); ++i) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    EntityItem item = world.dropItem(x, y, z, itemStack);
                    item.xd *= 0.5;
                    item.yd *= 0.5;
                    item.zd *= 0.5;
                    item.delayBeforeCanPickup = 0;
                }
            }
        }
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        dropContents(world, x, y, z);
        super.onBlockRemoved(world, x, y, z, data);
    }

    // Static Methods

    private static BlockGenerator instance = null;

    private static void setupInstance(BlockGenerator machine) {
        instance = machine;
    }

    private static BlockGenerator getInstance() {
        if (instance == null)
            throw new NullPointerException("Instance of BlockGenerator hasn't been setup!");
        return instance;
    }

    public static void updateBlockState(boolean active, World world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if (tileEntity == null)
            world.setBlockWithNotify(x, y, z, 0);
        else {
            getInstance().keepInventory = true;
            if (active)
                world.setBlockMetadataWithNotify(x, y, z, 1);
            if (!active)
                world.setBlockMetadataWithNotify(x, y, z, 0);

            getInstance().keepInventory = false;
            world.setBlockMetadataWithNotify(x, y, z, metadata);
            tileEntity.validate();
            world.setBlockTileEntity(x, y, z, tileEntity);
        }
    }
}
