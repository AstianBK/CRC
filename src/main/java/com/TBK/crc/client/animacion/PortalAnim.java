
package com.TBK.crc.client.animacion;// Save this class in your mod and generate all required imports

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/**
 * Made with Blockbench 5.0.1
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author Author
 */
public class PortalAnim {

	public static final AnimationDefinition animation = AnimationDefinition.Builder.withLength(4.0F).looping()
		.addAnimation("background", new AnimationChannel(AnimationChannel.Targets.SCALE,
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(0.95F, 0.95F, 0.95F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.scaleVec(0.95F, 0.85F, 0.95F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.scaleVec(0.95F, 0.95F, 0.95F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("portal1", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.5F, 1.5F, 1.5F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("portal2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 180.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("portal2", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.35F, 1.35F, 1.35F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("portal3", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 90.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 180.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("portal3", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.25F, 1.25F, 1.25F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("portal4", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -90.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("portal4", new AnimationChannel(AnimationChannel.Targets.SCALE, 
			new Keyframe(0.0F, KeyframeAnimations.scaleVec(1.6F, 1.6F, 1.6F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();
}