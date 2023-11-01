package turniplabs.industry.blocks.machines.lv

import net.minecraft.core.block.BlockTileEntityRotatable
import net.minecraft.core.block.entity.TileEntity
import net.minecraft.core.block.material.Material
import net.minecraft.core.entity.player.EntityPlayer
import net.minecraft.core.enums.EnumDropCause
import net.minecraft.core.item.ItemStack
import net.minecraft.core.util.helper.Side
import net.minecraft.core.util.helper.Sides
import net.minecraft.core.world.World
import net.minecraft.core.world.WorldSource
import sunsetsatellite.energyapi.interfaces.mixins.IEntityPlayer
import turniplabs.halplibe.helper.TextureHelper
import turniplabs.industry.Industry2
import turniplabs.industry.blocks.IndustryBlocks
import turniplabs.industry.blocks.entities.lv.TileEntityRecycler

class BlockRecycler(key: String?, id: Int, material: Material?) : BlockTileEntityRotatable(key, id, material) {
    private var keepInventory = false

    private val machineTexture: Array<IntArray> = arrayOf(
        TextureHelper.getOrCreateBlockTexture(Industry2.MOD_ID, "machine_compressor.png"),
        TextureHelper.getOrCreateBlockTexture(Industry2.MOD_ID, "machine_compressor_on.png"),
        TextureHelper.getOrCreateBlockTexture(Industry2.MOD_ID, "machine_casing_basic.png"),
        TextureHelper.getOrCreateBlockTexture(Industry2.MOD_ID, "machine_recycler.png")
    )

    init {
        setupInstance(this)
    }

    override fun getNewBlockEntity(): TileEntity {
        return TileEntityRecycler()
    }

    override fun blockActivated(world: World?, x: Int, y: Int, z: Int, player: EntityPlayer?): Boolean {
        if (!world!!.isClientSide) {
            val tileEntity: TileEntityRecycler = world.getBlockTileEntity(x, y, z) as TileEntityRecycler

            tileEntity ?: return false
            (player as IEntityPlayer).displayGuiScreen_energyapi(tileEntity)
        }
        return true
    }

    override fun getBlockTexture(blockAccess: WorldSource?, x: Int, y: Int, z: Int, side: Side): Int {

        /*
        0 = bottom
        1 = top
        2 = north
        3 = south
        4 = west
        5 = east
         */

        val tileEntity: TileEntityRecycler = blockAccess?.getBlockTileEntity(x, y, z) as TileEntityRecycler
        val metadata: Int = blockAccess.getBlockMetadata(x, y, z)
        val index = Sides.orientationLookUpHorizontal[6 * metadata + side.id]
        if (index != 2 && index != 1)
            texCoordToIndex(machineTexture[2][0], machineTexture[2][1]).also { atlasIndices[index] = it }

        if (index == 1)
            texCoordToIndex(machineTexture[3][0], machineTexture[3][1].also { atlasIndices[index] = it })

        if (index == 2)
            if (tileEntity.active) texCoordToIndex(machineTexture[1][0], machineTexture[1][1]).also { atlasIndices[index] = it }
            else texCoordToIndex(machineTexture[0][0], machineTexture[0][1]).also { atlasIndices[index] = it }

        return atlasIndices[index]
    }

    override fun getBreakResult(
        world: World?,
        dropCause: EnumDropCause?,
        x: Int,
        y: Int,
        z: Int,
        meta: Int,
        tileEntity: TileEntity?
    ): Array<ItemStack> {
        return when (dropCause) {
            EnumDropCause.SILK_TOUCH, EnumDropCause.PICK_BLOCK, EnumDropCause.PROPER_TOOL -> arrayOf(ItemStack(this))
            else ->  arrayOf(ItemStack(IndustryBlocks.machineCasing))
        }
    }

    companion object {
        private var instance: BlockRecycler? = null

        private fun setupInstance(machine: BlockRecycler) {
            instance = machine
        }

        private fun getInstance(): BlockRecycler {
            return instance ?: throw NullPointerException("Instance of BlockRecycler hasn't been setup!")
        }

        fun updateBlockState(active: Boolean, world: World, x: Int, y: Int, z: Int) {
            val metadata: Int = world.getBlockMetadata(x, y, z)
            val tileEntity: TileEntity? = world.getBlockTileEntity(x, y, z)

            if (tileEntity == null)
                world.setBlockWithNotify(x, y, z, 0)
            else {
                getInstance().keepInventory = true
                if (active) world.setBlockMetadataWithNotify(x, y, z, 1)
                if (!active) world.setBlockMetadataWithNotify(x, y, z, 0)

                getInstance().keepInventory = false
                world.setBlockMetadataWithNotify(x, y, z, metadata)
                tileEntity.validate()
                world.setBlockTileEntity(x, y, z, tileEntity)
            }
        }
    }
}