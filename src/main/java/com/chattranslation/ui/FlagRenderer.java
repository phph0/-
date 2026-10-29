package com.chattranslation.ui;

import net.minecraft.client.gui.DrawContext;

import java.util.HashMap;
import java.util.Map;

public class FlagRenderer {
    private static final int FLAG_WIDTH = 16;
    private static final int FLAG_HEIGHT = 11;
    private static final Map<String, int[][]> FLAG_DATA = new HashMap<>();

    static {
        buildFlags();
    }

    private static void buildFlags() {
        // Korea (KR) - Taegeukgi simplified: white bg, red/blue center circle
        FLAG_DATA.put("KR", new int[][]{
                {0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF},
                {0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF},
                {0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF},
                {0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF},
                {0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0x0047A0, 0x0047A0, 0xCD2E3A, 0xCD2E3A, 0xCD2E3A, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF},
                {0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0x0047A0, 0x0047A0, 0x0047A0, 0x0047A0, 0x0047A0, 0x0047A0, 0x0047A0, 0x0047A0, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF},
                {0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0x0047A0, 0x0047A0, 0x0047A0, 0xCD2E3A, 0xCD2E3A, 0x0047A0, 0x0047A0, 0x0047A0, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF},
                {0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0x0047A0, 0x0047A0, 0x0047A0, 0x0047A0, 0x0047A0, 0x0047A0, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF},
                {0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0x0047A0, 0x0047A0, 0x0047A0, 0x0047A0, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF},
                {0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF},
                {0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF},
        });

        // United States (US)
        FLAG_DATA.put("US", buildStripeFlag(
                new int[]{0xB22234, 0xFFFFFF, 0xB22234, 0xFFFFFF, 0xB22234, 0xFFFFFF, 0xB22234, 0xFFFFFF, 0xB22234, 0xFFFFFF, 0xB22234},
                0x3C3B6E, 6, 7
        ));

        // Japan (JP) - white with red circle
        FLAG_DATA.put("JP", buildSolidWithCircle(0xFFFFFF, 0xBC002D));

        // China (CN) - red background, yellow stars simplified
        FLAG_DATA.put("CN", buildSolidFlag(0xDE2910, 0xFFDE00, true));

        // France (FR) - blue white red vertical
        FLAG_DATA.put("FR", buildVerticalTricolor(0x002395, 0xFFFFFF, 0xED2939));

        // Germany (DE) - black red gold horizontal
        FLAG_DATA.put("DE", buildHorizontalTricolor(0x000000, 0xDD0000, 0xFFCC00));

        // Russia (RU) - white blue red
        FLAG_DATA.put("RU", buildHorizontalTricolor(0xFFFFFF, 0x0039A6, 0xD52B1E));

        // Italy (IT)
        FLAG_DATA.put("IT", buildVerticalTricolor(0x009246, 0xFFFFFF, 0xCE2B37));

        // Spain (ES)
        FLAG_DATA.put("ES", buildSpainFlag());

        // United Kingdom (GB)
        FLAG_DATA.put("GB", buildUKFlag());

        // Brazil (BR)
        FLAG_DATA.put("BR", buildBrazilFlag());

        // India (IN)
        FLAG_DATA.put("IN", buildHorizontalTricolor(0xFF9933, 0xFFFFFF, 0x138808));

        // Taiwan (TW) - simplified: red with blue canton
        FLAG_DATA.put("TW", buildTaiwanFlag());

        // Thailand (TH) - red white blue white red
        FLAG_DATA.put("TH", buildThailandFlag());

        // Vietnam (VN) - red with yellow star
        FLAG_DATA.put("VN", buildSolidFlag(0xDA251D, 0xFFFF00, true));

        // Turkey (TR) - red with white crescent
        FLAG_DATA.put("TR", buildSolidWithCircle(0xE30A17, 0xFFFFFF));

        // Netherlands (NL)
        FLAG_DATA.put("NL", buildHorizontalTricolor(0xAE1C28, 0xFFFFFF, 0x21468B));

        // Sweden (SE)
        FLAG_DATA.put("SE", buildNordicCross(0x006AA7, 0xFECC00));

        // Norway (NO)
        FLAG_DATA.put("NO", buildNordicCross(0xEF2B2D, 0xFFFFFF));

        // Denmark (DK)
        FLAG_DATA.put("DK", buildNordicCross(0xC60C30, 0xFFFFFF));

        // Finland (FI)
        FLAG_DATA.put("FI", buildNordicCross(0xFFFFFF, 0x003580));

        // Poland (PL)
        int[][] pl = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            int color = y < FLAG_HEIGHT / 2 ? 0xFFFFFF : 0xDC143C;
            for (int x = 0; x < FLAG_WIDTH; x++) pl[y][x] = color;
        }
        FLAG_DATA.put("PL", pl);

        // Portugal (PT)
        FLAG_DATA.put("PT", buildVerticalBicolor(0x006600, 0xFF0000, 6));

        // Ukraine (UA)
        int[][] ua = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            int color = y < FLAG_HEIGHT / 2 ? 0x005BBB : 0xFFD500;
            for (int x = 0; x < FLAG_WIDTH; x++) ua[y][x] = color;
        }
        FLAG_DATA.put("UA", ua);

        // Indonesia (ID) - red and white
        int[][] id = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            int color = y < FLAG_HEIGHT / 2 ? 0xFF0000 : 0xFFFFFF;
            for (int x = 0; x < FLAG_WIDTH; x++) id[y][x] = color;
        }
        FLAG_DATA.put("ID", id);

        // Saudi Arabia (SA) - green
        int[][] sa = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) sa[y][x] = 0x006C35;
        FLAG_DATA.put("SA", sa);

        // Israel (IL) - white with blue stripes
        int[][] il = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            int color = (y <= 1 || y >= 9) ? 0x0038B8 : 0xFFFFFF;
            for (int x = 0; x < FLAG_WIDTH; x++) il[y][x] = color;
        }
        FLAG_DATA.put("IL", il);
    }

    private static int[][] buildHorizontalTricolor(int top, int mid, int bot) {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            int color;
            if (y < 4) color = top;
            else if (y < 8) color = mid;
            else color = bot;
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = color;
        }
        return data;
    }

    private static int[][] buildVerticalTricolor(int left, int mid, int right) {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            for (int x = 0; x < FLAG_WIDTH; x++) {
                if (x < 5) data[y][x] = left;
                else if (x < 11) data[y][x] = mid;
                else data[y][x] = right;
            }
        }
        return data;
    }

    private static int[][] buildVerticalBicolor(int left, int right, int splitX) {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            for (int x = 0; x < FLAG_WIDTH; x++) {
                data[y][x] = x < splitX ? left : right;
            }
        }
        return data;
    }

    private static int[][] buildStripeFlag(int[] stripeColors, int cantonColor, int cantonH, int cantonW) {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            int stripe = stripeColors[y % stripeColors.length];
            for (int x = 0; x < FLAG_WIDTH; x++) {
                if (y < cantonH && x < cantonW) {
                    data[y][x] = cantonColor;
                } else {
                    data[y][x] = stripe;
                }
            }
        }
        return data;
    }

    private static int[][] buildSolidWithCircle(int bg, int circleColor) {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        int cx = FLAG_WIDTH / 2, cy = FLAG_HEIGHT / 2;
        int r = 3;
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            for (int x = 0; x < FLAG_WIDTH; x++) {
                int dx = x - cx, dy = y - cy;
                data[y][x] = (dx * dx + dy * dy <= r * r) ? circleColor : bg;
            }
        }
        return data;
    }

    private static int[][] buildSolidFlag(int bg, int accent, boolean hasStar) {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = bg;
        if (hasStar) {
            int cx = FLAG_WIDTH / 3, cy = FLAG_HEIGHT / 2;
            for (int dy = -1; dy <= 1; dy++)
                for (int dx = -1; dx <= 1; dx++)
                    if (cy + dy >= 0 && cy + dy < FLAG_HEIGHT && cx + dx >= 0 && cx + dx < FLAG_WIDTH)
                        data[cy + dy][cx + dx] = accent;
        }
        return data;
    }

    private static int[][] buildSpainFlag() {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            int color;
            if (y < 3) color = 0xAA151B;
            else if (y < 8) color = 0xF1BF00;
            else color = 0xAA151B;
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = color;
        }
        return data;
    }

    private static int[][] buildUKFlag() {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = 0x012169;
        // horizontal cross
        for (int x = 0; x < FLAG_WIDTH; x++) {
            data[4][x] = 0xFFFFFF; data[5][x] = 0xCF142B; data[6][x] = 0xFFFFFF;
        }
        // vertical cross
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            data[y][7] = 0xFFFFFF; data[y][8] = 0xCF142B; data[y][9] = 0xFFFFFF;
        }
        return data;
    }

    private static int[][] buildBrazilFlag() {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = 0x009C3B;
        // yellow diamond
        int cx = 8, cy = 5;
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            for (int x = 0; x < FLAG_WIDTH; x++) {
                int dx = Math.abs(x - cx), dy = Math.abs(y - cy);
                if (dx * 5 + dy * 8 < 28) data[y][x] = 0xFFDF00;
            }
        }
        // blue circle
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            for (int x = 0; x < FLAG_WIDTH; x++) {
                int dx = x - cx, dy = y - cy;
                if (dx * dx + dy * dy <= 4) data[y][x] = 0x002776;
            }
        }
        return data;
    }

    private static int[][] buildTaiwanFlag() {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = 0xFE0000;
        for (int y = 0; y < 6; y++)
            for (int x = 0; x < 8; x++) data[y][x] = 0x000095;
        data[2][3] = 0xFFFFFF; data[2][4] = 0xFFFFFF;
        data[3][3] = 0xFFFFFF; data[3][4] = 0xFFFFFF;
        return data;
    }

    private static int[][] buildThailandFlag() {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        int[] bands = {0xED1C24, 0xFFFFFF, 0x241D4F, 0x241D4F, 0x241D4F, 0x241D4F, 0x241D4F, 0xFFFFFF, 0xED1C24, 0xED1C24, 0xED1C24};
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            int color;
            if (y < 2) color = 0xED1C24;
            else if (y < 3) color = 0xFFFFFF;
            else if (y < 8) color = 0x241D4F;
            else if (y < 9) color = 0xFFFFFF;
            else color = 0xED1C24;
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = color;
        }
        return data;
    }

    private static int[][] buildNordicCross(int bg, int crossColor) {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = bg;
        // horizontal bar
        for (int x = 0; x < FLAG_WIDTH; x++) {
            if (4 < FLAG_HEIGHT) data[4][x] = crossColor;
            if (5 < FLAG_HEIGHT) data[5][x] = crossColor;
            if (6 < FLAG_HEIGHT) data[6][x] = crossColor;
        }
        // vertical bar
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            data[y][5] = crossColor;
            data[y][6] = crossColor;
            data[y][7] = crossColor;
        }
        return data;
    }

    public static void renderFlag(DrawContext context, String region, int x, int y) {
        int[][] flagData = FLAG_DATA.get(region);
        if (flagData == null) {
            renderDefaultFlag(context, x, y);
            return;
        }
        for (int fy = 0; fy < FLAG_HEIGHT && fy < flagData.length; fy++) {
            for (int fx = 0; fx < FLAG_WIDTH && fx < flagData[fy].length; fx++) {
                int color = 0xFF000000 | flagData[fy][fx];
                context.fill(x + fx, y + fy, x + fx + 1, y + fy + 1, color);
            }
        }
        // border
        context.drawBorder(x - 1, y - 1, FLAG_WIDTH + 2, FLAG_HEIGHT + 2, 0xFF333333);
    }

    private static void renderDefaultFlag(DrawContext context, int x, int y) {
        context.fill(x, y, x + FLAG_WIDTH, y + FLAG_HEIGHT, 0xFFAAAAAA);
        context.fill(x + 6, y + 4, x + 10, y + 7, 0xFFFFFFFF);
        context.drawBorder(x - 1, y - 1, FLAG_WIDTH + 2, FLAG_HEIGHT + 2, 0xFF333333);
    }

    public static int getFlagWidth() {
        return FLAG_WIDTH;
    }

    public static int getFlagHeight() {
        return FLAG_HEIGHT;
    }
}
