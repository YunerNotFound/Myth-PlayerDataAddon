package com.playerdataaddon.Listener;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.xbaimiao.invsync.api.events.InvSyncPluginDataSaveEvent;
import com.xbaimiao.invsync.api.events.InvSyncPluginDataSyncEvent;
import com.xbaimiao.invsync.bukkit.setting.SyncSetting;
import com.xbaimiao.invsync.shadow.easylib.util.EListener;
import com.xbaimiao.invsync.shadow.easylib.util.UtilsKt;
import com.xbaimiao.invsync.shadow.kotlin.Metadata;
import com.xbaimiao.invsync.shadow.kotlin.Unit;
import com.xbaimiao.invsync.shadow.kotlin.io.CloseableKt;
import com.xbaimiao.invsync.shadow.kotlin.jvm.internal.Intrinsics;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Objects;

@EListener
@Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007J\u0010\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0013H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00068BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0018\u0010\t\u001a\u00020\n*\u00020\u000b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0014"}, d2 = {"Lcom/xbaimiao/invsync/bukkit/core/hook/SyncCMIFlyCharge;", "Lorg/bukkit/event/Listener;", "()V", "KEY", "", "hasCMI", "", "getHasCMI", "()Z", "adaptCMI", "Lcom/Zrips/CMI/Containers/CMIUser;", "Lorg/bukkit/OfflinePlayer;", "getAdaptCMI", "(Lorg/bukkit/OfflinePlayer;)Lcom/Zrips/CMI/Containers/CMIUser;", "save", "", "event", "Lcom/xbaimiao/invsync/api/events/InvSyncPluginDataSaveEvent;", "sync", "Lcom/xbaimiao/invsync/api/events/InvSyncPluginDataSyncEvent;", "InvSync"})
public final class SyncCMIPlayerNameplate implements Listener {
    @NotNull
    public static final SyncCMIPlayerNameplate INSTANCE = new SyncCMIPlayerNameplate();
    @NotNull
    private static final String KEY = "CMIPlayerNameplate";

    public SyncCMIPlayerNameplate() {
    }

    private boolean getHasCMI() {
        return !Bukkit.getPluginManager().isPluginEnabled("CMI");
    }

    @EventHandler
    public void save(@NotNull InvSyncPluginDataSaveEvent event) throws IOException {
        Intrinsics.checkNotNullParameter(event, "event");
        if (!SyncSetting.INSTANCE.isSyncCMIFlyCharge() || this.getHasCMI()) {
            return;
        }
        Player player = event.getPlayer();
        CMIUser cmiUser = this.getAdaptCMI(player != null ? (OfflinePlayer) player : event.getOfflinePlayer());
        String namePlatePrefix = cmiUser.getNamePlatePrefix();
        String namePlateSuffix = cmiUser.getNamePlateSuffix();

        String namePlatePrefixExist = (namePlatePrefix != null) ? namePlatePrefix : null;
        String namePlateSuffixExist = (namePlateSuffix != null) ? namePlateSuffix : null;

        String namePlate = namePlatePrefixExist + "/" + namePlateSuffixExist;
        if (event.readData(KEY) == null && !SyncSetting.INSTANCE.getInitFlyCharge()) {
            return;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream((OutputStream) out);
        Throwable throwable = null;
        try {
            dataOutputStream.writeUTF(namePlate);
          //  dataOutputStream.writeUTF(namePlate);
            Unit unit = Unit.INSTANCE;
        } catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        } finally {
            CloseableKt.closeFinally((Closeable) dataOutputStream, throwable);
        }
        event.putData(KEY, out.toByteArray());
        Object[] debugParams = {event.getOfflinePlayer().getName() + " 玩家称号写入完成 " + namePlate};
        UtilsKt.debug(debugParams);
    }

    @EventHandler
    public void sync(@NotNull InvSyncPluginDataSyncEvent event) throws IOException {
        Intrinsics.checkNotNullParameter(event, "event");
        if (!SyncSetting.INSTANCE.isSyncCMIFlyCharge() || this.getHasCMI()) {
            return;
        }
        byte[] data = event.readData(KEY);
        if (data == null) {
            return;
        }
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        Closeable closeable = new DataInputStream(stream);
        Throwable throwable = null;
        try {
            DataInputStream dataInputStream = (DataInputStream) closeable;
            CMIUser cmiUser = INSTANCE.getAdaptCMI((OfflinePlayer) event.getPlayer());
            String namePlate = dataInputStream.readUTF();
            String[] split = namePlate.split("/");
            if (!Objects.equals(split[0], "null")) {
                cmiUser.setNamePlatePrefix(split[0]);
            }
            if (!Objects.equals(split[1], "null")) {
                cmiUser.setNamePlateSuffix(split[1]);
            }

//            FlightCharge flightCharge = cmiUser.getFlightCharge();
//            double value = dataInputStream.readDouble();
//            flightCharge.setCharge(value);
            Object[] debugParams = {event.getPlayer().getName() + " 玩家称号同步完成 value: " + namePlate};
            UtilsKt.debug(debugParams);
            Unit unit = Unit.INSTANCE;
        } catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        } finally {
            CloseableKt.closeFinally(closeable, throwable);
        }
    }

    private CMIUser getAdaptCMI(OfflinePlayer player) {
        CMIUser cmiUser = CMI.getInstance().getPlayerManager().getUser(player);
        Intrinsics.checkNotNullExpressionValue(cmiUser, "let(...)");
        return cmiUser;
    }
}
