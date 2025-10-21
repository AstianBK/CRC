package com.TBK.crc.common;

import com.TBK.crc.CRC;
import com.TBK.crc.common.screen.ImplantScreen;
import com.TBK.crc.server.capability.MultiArmCapability;
import com.TBK.crc.server.network.PacketHandler;
import com.TBK.crc.server.network.messager.PacketKeySync;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CRC.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeInputEvent {
    public static int selectActual = -1;
    public static int selectInitial = -1;
    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        onInput(mc, event.getKey(), event.getAction());

    }

    @SubscribeEvent
    public static void onMouseClick(InputEvent.MouseButton event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        if(onInput(mc, event.getButton(), event.getAction())){
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouseScrolling(InputEvent.MouseScrollingEvent event){
        Minecraft mc = Minecraft.getInstance();
        if(mc.level == null) return;
        if(CRCKeybinds.attackKey3.isDown()){
            event.setCanceled(true);
            assert MultiArmCapability.get(mc.player)!=null;
            MultiArmCapability capability = MultiArmCapability.get(mc.player);
            if(event.getScrollDelta()<0){
                selectActual = selectActual+1>=3 ? 0 : selectActual+1;
            }else {
                selectActual = selectActual-1<0 ? capability.skills.getSkills().size() : selectActual-1;
            }
        }
    }

    private static boolean onInput(Minecraft mc, int key, int action) {
        MultiArmCapability cap = MultiArmCapability.get(mc.player);
        if(cap!=null){
            if(CRCKeybinds.attackKey3.consumeClick() && !cap.hotbarActive){
                selectActual = cap.getPosSelectUpgrade();
                selectInitial = cap.getPosSelectUpgrade();
            }
            cap.hotbarActive = CRCKeybinds.attackKey3.isDown();
            if(CRCKeybinds.attackKey3.getKey().getValue() == key && action == 0){
                PacketHandler.sendToServer(new PacketKeySync(0x12,selectActual,-1));
            }

            if (mc.screen == null && (key==1) && cap.canUseSkill(cap.getSelectSkill())) {
                PacketHandler.sendToServer(new PacketKeySync(0x52,action,-1));

                return true;
            }else if(mc.screen == null && CRCKeybinds.bottomImplantStore.consumeClick()){
                PacketHandler.sendToServer(new PacketKeySync(key,action,-1));
            }

        }
        /*if(){
            CRC.LOGGER.debug("relese"+cooldownUse);
            cooldownUse = 0;
            CRC.LOGGER.debug("relese despues"+cooldownUse);
        }*/


        return false;
    }
}
