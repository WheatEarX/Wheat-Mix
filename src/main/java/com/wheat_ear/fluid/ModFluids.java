package com.wheat_ear.fluid;

import com.wheat_ear.WheatMix;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModFluids {
    public static final FlowableFluid FLOWING_PAPER = Registry.register(Registries.FLUID,
            new Identifier(WheatMix.MOD_ID, "flowing_paper"),
            new PaperFluid.Flowing());
    public static final FlowableFluid PAPER = Registry.register(Registries.FLUID,
            new Identifier(WheatMix.MOD_ID, "paper"),
            new PaperFluid.Still());
}
