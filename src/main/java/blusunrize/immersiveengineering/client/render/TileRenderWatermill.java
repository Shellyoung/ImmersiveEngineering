package blusunrize.immersiveengineering.client.render;

import blusunrize.immersiveengineering.common.blocks.wooden.TileEntityWatermill;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.OBJState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import org.lwjgl.opengl.GL11;

public class TileRenderWatermill extends TileEntitySpecialRenderer<TileEntityWatermill>
//public class TileRenderWatermill extends FastTESR<TileEntityWatermill>
{
	@Override
	public void renderTileEntityAt(TileEntityWatermill tile, double x, double y, double z, float partialTicks, int destroyStage)
//	public void renderTileEntityFast(TileEntityWatermill tile, double x, double y, double z, float partialTicks, int destroyStage, VertexBuffer vertexBuffer)
	{
		if (tile.isDummy()||!tile.getWorld().isBlockLoaded(tile.getPos(), false))
			return;
		final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		IBlockState state = tile.getWorld().getBlockState(tile.getPos());
		BlockPos blockPos = tile.getPos();
		IBakedModel model = blockRenderer.getModelForState(state);
		if(state instanceof IExtendedBlockState)
			state = ((IExtendedBlockState)state).withProperty(Properties.AnimationProperty, new OBJState(Lists.newArrayList(OBJModel.Group.ALL), true));

		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldRenderer = tessellator.getBuffer();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		RenderHelper.disableStandardItemLighting();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.enableBlend();
		GlStateManager.disableCull();
		if(Minecraft.isAmbientOcclusionEnabled())
			GlStateManager.shadeModel(7425);
		else
			GlStateManager.shadeModel(7424);
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + .5, y + .5, z + .5);
		GlStateManager.rotate(90, 1, 0, 0);

		final float dir = tile.facing == EnumFacing.NORTH ? 180 : tile.facing == EnumFacing.SOUTH ? 0 : tile.facing == EnumFacing.WEST ? 90 : -90;
		float wheelRotation = 360 * tile.rotation - (!tile.canTurn || tile.rotation == 0 || tile.rotation - tile.prevRotation < 4 ? 0 : tile.facing.getAxis() == Axis.X ? -partialTicks : partialTicks);
		if(tile.facing.getAxisDirection() == AxisDirection.NEGATIVE)
			wheelRotation *= -1;
		final float rot = wheelRotation;
		GlStateManager.rotate(dir, 0, 0, 1);
		GlStateManager.rotate(rot, 0, 1, 0);

//		vertexBuffer.setTranslation(x - blockPos.getX(), y - blockPos.getY(), z - blockPos.getZ());
//		final Matrix4 mat = new Matrix4();
//		mat.rotate(Math.toRadians(90), 1, 0, 0);
//		mat.rotate(Math.toRadians(dir), 0, 0, 1);
//		mat.rotate(Math.toRadians(rot), 0, 1, 0);
//		IVertexTransformer transformer = (quad, type, usage, data) ->
//		{
//			if(usage == EnumUsage.POSITION)
//			{
//				Vector3f pos = new Vector3f(data);
//				pos.sub(new Vector3f(0.5F, 0.5F, 0.5F));
//				mat.apply(pos);
//				pos.add(new Vector3f(0.5F, 0.5F, 0.5F));
//				pos.get(data);
//			}
//			return data;
//		};
//		blockRenderer.getBlockModelRenderer().renderModel(tile.getWorld(), BakedModelTransformer.transform(model, transformer, state, 0), state, tile.getPos(), vertexBuffer, true);
//		vertexBuffer.setTranslation(0, 0, 0);

		worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		worldRenderer.setTranslation(-.5 - blockPos.getX(), -.5 - blockPos.getY(), -.5 - blockPos.getZ());
		worldRenderer.color(255, 255, 255, 255);
		blockRenderer.getBlockModelRenderer().renderModel(tile.getWorld(), model, state, tile.getPos(), worldRenderer, true);
		worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
		tessellator.draw();
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();

		//		TileEntityWatermill wheel = (TileEntityWatermill)tile;
		//		if(wheel.offset[0]!=0||wheel.offset[1]!=0)
		//			return;
		//
		//		GL11.glPushMatrix();
		//		GL11.glTranslated(x+.5, y+.5, z+.5);
		//
		//		if(wheel.facing==4||wheel.facing==5)
		//			GL11.glRotated(90, 0, 1, 0);
		//
		//		model.setRotateAngle(model.Axle, 0, 0, (float)Math.toRadians(rot));
		//		ClientUtils.bindTexture("immersiveengineering:textures/models/watermill.png");
		//		model.render(null, 0, 0, 0, 0, 0, .0625f);
		//
		//		GL11.glPopMatrix();
	}

}