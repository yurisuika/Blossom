package dev.yurisuika.blossom.command.argument;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.StringIdentifiable;

public class PrecipitationArgumentType extends EnumArgumentType<PrecipitationArgumentType.Precipitation> {

    public PrecipitationArgumentType() {
        super(Precipitation.CODEC, Precipitation::values);
    }

    public static EnumArgumentType<Precipitation> precipitation() {
        return new PrecipitationArgumentType();
    }

    public static Precipitation getPrecipitation(CommandContext<ServerCommandSource> context, String id) {
        return context.getArgument(id, Precipitation.class);
    }

    public enum Precipitation implements StringIdentifiable {

        NONE("none") {},
        RAIN("rain") {},
        SNOW("snow") {};
        
        private final String name;
        public static final Codec<Precipitation> CODEC;

        Precipitation(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }

        static {
            CODEC = StringIdentifiable.createCodec(Precipitation::values);
        }
        
    }
    
}