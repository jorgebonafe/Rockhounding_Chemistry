package com.globbypotato.rockhounding_chemistry.machines.tileentity;

import com.globbypotato.rockhounding_chemistry.ModContents;
import com.globbypotato.rockhounding_chemistry.machines.LaserBeam;
import com.globbypotato.rockhounding_chemistry.machines.LaserTX;

import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityLaserTX extends TileEntity implements ITickable {
	private int countBeam;
	private int maxRange = 30;
	private boolean firstObstacle;

	private boolean isPowered(IBlockState state, EnumFacing enumfacing) {
	    if(worldObj.getBlockState(pos.offset(enumfacing, 1)).getBlock() == Blocks.POWERED_REPEATER  ){
		    EnumFacing repeaterfacing = worldObj.getBlockState(pos.offset(enumfacing, 1)).getValue(BlockRedstoneRepeater.FACING);
		    if(enumfacing == repeaterfacing){
		    	return true;
		    }
	    }
	    return false;
	}

	@Override
	public void update() {
		if(!worldObj.isRemote){
		    IBlockState state = worldObj.getBlockState(pos);
		    EnumFacing enumfacing = state.getValue(LaserTX.FACING);
		    EnumFacing beamFacing = enumfacing.getOpposite();
			firstObstacle = false;
			if(countBeam >= maxRange){
				if(isPowered(state, enumfacing)){
		    		for(int x = 1; x <= maxRange; x++){
		    			BlockPos checkingPos = pos.offset(enumfacing.getOpposite(), x);
						IBlockState checkingState = worldObj.getBlockState(checkingPos); 
		    			if(firstObstacle == false){
						    if(checkingState.getBlock() == Blocks.AIR){
								IBlockState beamState = ModContents.laserBeam.getDefaultState().withProperty(LaserBeam.FACING, beamFacing); 
						    	worldObj.setBlockState(checkingPos, beamState);
					    		firstObstacle = false;
						    }else if(checkingState.getBlock() == ModContents.laserBeam){
						    	if(checkingState.getValue(LaserBeam.FACING) == beamFacing ) {
						    		firstObstacle = false;
							    }else{
							    	firstObstacle = true;
						    	}
						    }else{
						    	firstObstacle = true;
						    }
		    			}
		    		}
			    }else{
			    	if(worldObj.getBlockState(pos.offset(enumfacing.getOpposite(), 1)).getBlock() == ModContents.laserBeam){
			    		worldObj.setBlockState(pos.offset(enumfacing.getOpposite(), 1), Blocks.AIR.getDefaultState());
			    	}
			    }
				countBeam = 0;
			}else{
				countBeam++;
			}
		}
	}

}
