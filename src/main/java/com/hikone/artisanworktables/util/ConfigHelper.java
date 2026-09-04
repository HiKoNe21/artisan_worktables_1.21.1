package com.hikone.artisanworktables.util;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Path;

public final class ConfigHelper
{
    public static void loadConfig(ModConfigSpec spec, Path path)
    {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        configData.load();

        if (!spec.isCorrect(configData))
        {
            spec.correct(configData);
            configData.save();
        }
    }

    private ConfigHelper()
    {
    }
}
