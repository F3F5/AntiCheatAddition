package de.photon.anticheataddition.modules.additions.informationhider;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEquipment;
import de.photon.anticheataddition.user.User;
import de.photon.anticheataddition.util.log.Log;

public final class ItemCountHider extends InformationHiderModule
{
    public static final ItemCountHider INSTANCE = new ItemCountHider();

    private ItemCountHider()
    {
        super("InformationHider.parts.item_count");
    }

    @Override
    protected void hideInformation(User user, WrapperPlayServerEntityEquipment wrapper, int entityId, ItemStack item)
    {
        Log.finer(() -> "ItemCountHider entity id: " + entityId + " with item " + item);

        if (item.getAmount() <= 1) return;
        Log.finer(() -> "Hiding item count of " + item + " for entity id " + entityId);
        item.setAmount(1);
    }
}
