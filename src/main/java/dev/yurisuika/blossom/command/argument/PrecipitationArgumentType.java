package dev.yurisuika.blossom.command.argument;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.StringIdentifiable;

public class PrecipitationArgumentType extends EnumArgumentType<PrecipitationArgumentType.Precipitation> {

    public PrecipitationArgumentType() {
        super(PrecipitationArgumentType.Precipitation.CODEC, PrecipitationArgumentType.Precipitation::values);
    }

    public static EnumArgumentType<PrecipitationArgumentType.Precipitation> precipitation() {
        return new PrecipitationArgumentType();
    }

    public static PrecipitationArgumentType.Precipitation getPrecipitation(CommandContext<ServerCommandSource> context, String id) {
        return context.getArgument(id, PrecipitationArgumentType.Precipitation.class);
    }

    public enum Precipitation implements StringIdentifiable {

        NONE("none") {},
        RAIN("rain") {},
        SNOW("snow") {};
        
        private final String name;
        public static final Codec<PrecipitationArgumentType.Precipitation> CODEC;

        Precipitation(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        static {
            CODEC = StringIdentifiable.createCodec(PrecipitationArgumentType.Precipitation::values);
        }
        
    }
    
}