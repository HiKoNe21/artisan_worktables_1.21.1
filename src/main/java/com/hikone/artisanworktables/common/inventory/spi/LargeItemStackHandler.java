package com.hikone.artisanworktables.common.inventory.spi;

import com.hikone.artisanworktables.util.StackHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

public class LargeItemStackHandler extends ItemStackHandler
{
    public LargeItemStackHandler()
    {
    }

    public LargeItemStackHandler(int size)
    {
        super(size);
    }

    public LargeItemStackHandler(NonNullList<ItemStack> stacks)
    {
        super(stacks);
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
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag)
    {
        setSize(tag.contains("Size", Tag.TAG_INT) ? tag.getInt("Size") : this.stacks.size());
        ListTag tagList = tag.getList("Items", Tag.TAG_COMPOUND);

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
