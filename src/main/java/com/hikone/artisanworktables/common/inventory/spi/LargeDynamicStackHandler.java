package com.hikone.artisanworktables.common.inventory.spi;

import com.hikone.artisanworktables.util.StackHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class LargeDynamicStackHandler extends DynamicStackHandler
{
    public LargeDynamicStackHandler(int initialSize)
    {
        super(initialSize);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider)
    {
        ListTag nbtTagList = new ListTag();

        for (int i = 0; i < this.stacks.size(); i++)
        {
            if (!this.stacks.get(i).isEmpty())
            {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                StackHelper.writeLargeItemStack(this.stacks.get(i), itemTag, provider);
                nbtTagList.add(itemTag);
            }
        }

        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", this.stacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
    {
        setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : this.stacks.size());
        ListTag tagList = nbt.getList("Items", Tag.TAG_COMPOUND);

        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundTag itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");

            if (slot >= 0 && slot < this.stacks.size())
            {
                this.stacks.set(slot, StackHelper.readLargeItemStack(itemTags, provider));
            }
        }
        this.onLoad();
    }
}
