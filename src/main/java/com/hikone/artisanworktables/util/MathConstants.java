package com.hikone.artisanworktables.util;

import java.util.Random;

public final class MathConstants
{
    private MathConstants()
    {
    }

    public static final double DBL_EPSILON = 2.220446049250313E-16d;

    public static final float FLT_EPSILON = 1.1920928955078125E-7f;

    public static final float FLT_ONE_MINUS_EPSILON = 1.0f - FLT_EPSILON;

    public static final float ZERO_TOLERANCE = 0.0001f;

    public static final float ONE_THIRD = 1.0f / 3.0f;

    public static final float E = (float) Math.E;

    public static final float PI = (float) Math.PI;

    public static final float TWO_PI = 2.0f * PI;

    public static final float HALF_PI = 0.5f * PI;

    public static final float QUARTER_PI = 0.25f * PI;

    public static final float THREE_QUARTER_PI = 0.75f * PI;

    public static final float INV_PI = 1.0f / PI;

    public static final float INV_TWO_PI = 1.0f / TWO_PI;

    public static final float PI_OVER_360 = PI / 360.0f;

    public static final float DEG_TO_RAD = PI / 180.0f;

    public static final float RAD_TO_DEG = 180.0f / PI;

    public static final float SQRT3 = 1.7320508075688772935274463415059f;

    public static final Random rand = new Random(System.currentTimeMillis());

    private static final int BIG_ENOUGH_INT = 16 * 1024;
    private static final double BIG_ENOUGH_FLOOR = BIG_ENOUGH_INT + 0.0000;
    private static final double BIG_ENOUGH_ROUND = BIG_ENOUGH_INT + 0.5000;
    private static final double BIG_ENOUGH_CEIL = BIG_ENOUGH_INT + 0.9999;
}
