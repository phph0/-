package com.ctl.ui;

import net.minecraft.client.gui.DrawContext;

import java.util.HashMap;
import java.util.Map;

public class FlagRenderer {
    private static final int FLAG_WIDTH = 16;
    private static final int FLAG_HEIGHT = 11;
    private static final Map<String, int[][]> FLAG_DATA = new HashMap<>();

    private static final int[][] STAR_5 = {
            {0, 0, 1, 0, 0},
            {0, 1, 1, 1, 0},
            {1, 1, 1, 1, 1},
            {0, 1, 1, 1, 0},
            {0, 0, 1, 0, 0},
    };

    static {
        buildFlags();
    }

    private static void buildFlags() {
        // Korea (KR) - Taegeukgi: white bg, red/blue yin-yang
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

        // Japan (JP) - white with red circle (hinomaru)
        FLAG_DATA.put("JP", buildSolidWithCircle(0xFFFFFF, 0xBC002D));

        // China (CN) - red field, big 5-point star + 4 small stars
        FLAG_DATA.put("CN", buildChinaFlag());

        // France (FR), Germany (DE), Russia (RU), Italy (IT), Netherlands (NL)
        // - genuinely plain stripe flags in real life, left as solid bands
        FLAG_DATA.put("FR", buildVerticalTricolor(0x002395, 0xFFFFFF, 0xED2939));
        FLAG_DATA.put("DE", buildHorizontalTricolor(0x000000, 0xDD0000, 0xFFCC00));
        FLAG_DATA.put("RU", buildHorizontalTricolor(0xFFFFFF, 0x0039A6, 0xD52B1E));
        FLAG_DATA.put("IT", buildVerticalTricolor(0x009246, 0xFFFFFF, 0xCE2B37));
        FLAG_DATA.put("NL", buildHorizontalTricolor(0xAE1C28, 0xFFFFFF, 0x21468B));

        // Spain (ES)
        FLAG_DATA.put("ES", buildSpainFlag());

        // United Kingdom (GB) - Union Jack with diagonals
        FLAG_DATA.put("GB", buildUKFlag());

        // Brazil (BR) - green/yellow/blue globe with equatorial band
        FLAG_DATA.put("BR", buildBrazilFlag());

        // India (IN) - tricolor + Ashoka Chakra hint
        int[][] india = buildHorizontalTricolor(0xFF9933, 0xFFFFFF, 0x138808);
        addChakra(india);
        FLAG_DATA.put("IN", india);

        // Taiwan (TW) - red field, blue canton with white sun
        FLAG_DATA.put("TW", buildTaiwanFlag());

        // Thailand (TH) - 5-band ratio already accurate
        FLAG_DATA.put("TH", buildThailandFlag());

        // Vietnam (VN) - red field, yellow 5-point star
        FLAG_DATA.put("VN", buildVietnamFlag());

        // Turkey (TR) - red field, white crescent + star
        FLAG_DATA.put("TR", buildTurkeyFlag());

        // Nordic crosses
        FLAG_DATA.put("SE", buildNordicCross(0x006AA7, 0xFECC00));
        FLAG_DATA.put("NO", buildNorwayFlag());
        FLAG_DATA.put("DK", buildNordicCross(0xC60C30, 0xFFFFFF));
        FLAG_DATA.put("FI", buildNordicCross(0xFFFFFF, 0x003580));

        // Poland (PL) - plain bicolor
        int[][] pl = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            int color = y < FLAG_HEIGHT / 2 ? 0xFFFFFF : 0xDC143C;
            for (int x = 0; x < FLAG_WIDTH; x++) pl[y][x] = color;
        }
        FLAG_DATA.put("PL", pl);

        // Portugal (PT) - green/red with small emblem hint at the seam
        int[][] pt = buildVerticalBicolor(0x006600, 0xFF0000, 6);
        addPortugalEmblem(pt);
        FLAG_DATA.put("PT", pt);

        // Ukraine (UA) - plain bicolor
        int[][] ua = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            int color = y < FLAG_HEIGHT / 2 ? 0x005BBB : 0xFFD500;
            for (int x = 0; x < FLAG_WIDTH; x++) ua[y][x] = color;
        }
        FLAG_DATA.put("UA", ua);

        // Indonesia (ID) - plain bicolor
        int[][] id = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            int color = y < FLAG_HEIGHT / 2 ? 0xFF0000 : 0xFFFFFF;
            for (int x = 0; x < FLAG_WIDTH; x++) id[y][x] = color;
        }
        FLAG_DATA.put("ID", id);

        // Saudi Arabia (SA) - green field with calligraphy/sword hint
        int[][] sa = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) sa[y][x] = 0x006C35;
        addSaudiMarks(sa);
        FLAG_DATA.put("SA", sa);

        // Israel (IL) - white field, blue stripes + Star of David
        int[][] il = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            int color = (y <= 1 || y >= 9) ? 0x0038B8 : 0xFFFFFF;
            for (int x = 0; x < FLAG_WIDTH; x++) il[y][x] = color;
        }
        addStarOfDavid(il);
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

    private static int[][] buildChinaFlag() {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = 0xDE2910;

        stampStar(data, 1, 1, 0xFFDE00);

        int[][] smallStars = {{8, 1}, {10, 3}, {10, 6}, {8, 8}};
        for (int[] s : smallStars) data[s[1]][s[0]] = 0xFFDE00;
        return data;
    }

    private static int[][] buildVietnamFlag() {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = 0xDA251D;

        stampStar(data, 6, 3, 0xFFFF00);
        return data;
    }

    private static void stampStar(int[][] data, int offsetX, int offsetY, int color) {
        for (int sy = 0; sy < STAR_5.length; sy++) {
            for (int sx = 0; sx < STAR_5[sy].length; sx++) {
                if (STAR_5[sy][sx] == 1) {
                    int px = offsetX + sx, py = offsetY + sy;
                    if (px >= 0 && px < FLAG_WIDTH && py >= 0 && py < FLAG_HEIGHT) {
                        data[py][px] = color;
                    }
                }
            }
        }
    }

    private static int[][] buildTurkeyFlag() {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = 0xE30A17;

        int cx1 = 5, cx2 = 7, cy = 5, r = 3;
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            for (int x = 0; x < FLAG_WIDTH; x++) {
                int dx1 = x - cx1, dy1 = y - cy;
                boolean inMoon = dx1 * dx1 + dy1 * dy1 <= r * r;
                int dx2 = x - cx2, dy2 = y - cy;
                boolean inBite = dx2 * dx2 + dy2 * dy2 <= r * r;
                if (inMoon && !inBite) data[y][x] = 0xFFFFFF;
            }
        }

        // small star beside the crescent
        data[5][10] = 0xFFFFFF;
        data[4][10] = 0xFFFFFF;
        data[6][10] = 0xFFFFFF;
        data[5][9] = 0xFFFFFF;
        data[5][11] = 0xFFFFFF;
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
        // tiny coat-of-arms hint on the hoist side of the yellow band
        data[4][2] = 0xAA151B;
        data[4][3] = 0xAA151B;
        data[5][2] = 0xAA151B;
        data[5][3] = 0xAA151B;
        return data;
    }

    private static int[][] buildUKFlag() {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = 0x012169;

        // diagonal saltire (approximated on the non-square grid)
        for (int x = 0; x < FLAG_WIDTH; x++) {
            int yA = (int) Math.floor(x * (FLAG_HEIGHT - 1) / (double) (FLAG_WIDTH - 1));
            int yB = (int) Math.ceil(x * (FLAG_HEIGHT - 1) / (double) (FLAG_WIDTH - 1));
            setIfBlue(data, x, yA);
            setIfBlue(data, x, yB);

            int yC = (int) Math.floor((FLAG_WIDTH - 1 - x) * (FLAG_HEIGHT - 1) / (double) (FLAG_WIDTH - 1));
            int yD = (int) Math.ceil((FLAG_WIDTH - 1 - x) * (FLAG_HEIGHT - 1) / (double) (FLAG_WIDTH - 1));
            setIfBlue(data, x, yC);
            setIfBlue(data, x, yD);
        }

        // St George's cross on top, red bordered white
        for (int x = 0; x < FLAG_WIDTH; x++) {
            data[4][x] = 0xFFFFFF;
            data[5][x] = 0xCF142B;
            data[6][x] = 0xFFFFFF;
        }
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            data[y][7] = 0xFFFFFF;
            data[y][8] = 0xCF142B;
            data[y][9] = 0xFFFFFF;
        }
        return data;
    }

    private static void setIfBlue(int[][] data, int x, int y) {
        if (x >= 0 && x < FLAG_WIDTH && y >= 0 && y < FLAG_HEIGHT && data[y][x] == 0x012169) {
            data[y][x] = 0xFFFFFF;
        }
    }

    private static int[][] buildBrazilFlag() {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = 0x009C3B;

        int cx = 8, cy = 5;
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            for (int x = 0; x < FLAG_WIDTH; x++) {
                int dx = Math.abs(x - cx), dy = Math.abs(y - cy);
                if (dx * 5 + dy * 8 < 28) data[y][x] = 0xFFDF00;
            }
        }
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            for (int x = 0; x < FLAG_WIDTH; x++) {
                int dx = x - cx, dy = y - cy;
                if (dx * dx + dy * dy <= 4) data[y][x] = 0x002776;
            }
        }
        // equatorial white band through the globe
        for (int x = cx - 2; x <= cx + 2; x++) {
            if (x >= 0 && x < FLAG_WIDTH) data[cy][x] = 0xFFFFFF;
        }
        return data;
    }

    private static int[][] buildTaiwanFlag() {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = 0xFE0000;

        // blue canton
        for (int y = 0; y < 6; y++)
            for (int x = 0; x < 8; x++) data[y][x] = 0x000095;

        // white sun: disc + 4 rays
        data[2][3] = 0xFFFFFF;
        data[2][4] = 0xFFFFFF;
        data[3][3] = 0xFFFFFF;
        data[3][4] = 0xFFFFFF;
        data[1][3] = 0xFFFFFF;
        data[1][4] = 0xFFFFFF;
        data[4][3] = 0xFFFFFF;
        data[4][4] = 0xFFFFFF;
        data[2][1] = 0xFFFFFF;
        data[3][1] = 0xFFFFFF;
        data[2][6] = 0xFFFFFF;
        data[3][6] = 0xFFFFFF;
        return data;
    }

    private static int[][] buildThailandFlag() {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
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
        for (int x = 0; x < FLAG_WIDTH; x++) {
            data[4][x] = crossColor;
            data[5][x] = crossColor;
            data[6][x] = crossColor;
        }
        for (int y = 0; y < FLAG_HEIGHT; y++) {
            data[y][5] = crossColor;
            data[y][6] = crossColor;
            data[y][7] = crossColor;
        }
        return data;
    }

    private static int[][] buildNorwayFlag() {
        int[][] data = new int[FLAG_HEIGHT][FLAG_WIDTH];
        for (int y = 0; y < FLAG_HEIGHT; y++)
            for (int x = 0; x < FLAG_WIDTH; x++) data[y][x] = 0xEF2B2D;

        // outer white cross (wider)
        for (int row : new int[]{3, 4, 5, 6, 7}) {
            for (int x = 0; x < FLAG_WIDTH; x++) data[row][x] = 0xFFFFFF;
        }
        for (int col : new int[]{4, 5, 6, 7, 8}) {
            for (int y = 0; y < FLAG_HEIGHT; y++) data[y][col] = 0xFFFFFF;
        }
        // inner navy cross (narrower, leaves a white border)
        for (int row : new int[]{4, 5, 6}) {
            for (int x = 0; x < FLAG_WIDTH; x++) data[row][x] = 0x1E2A5E;
        }
        for (int col : new int[]{5, 6, 7}) {
            for (int y = 0; y < FLAG_HEIGHT; y++) data[y][col] = 0x1E2A5E;
        }
        return data;
    }

    private static void addChakra(int[][] data) {
        int cx = FLAG_WIDTH / 2, cy = FLAG_HEIGHT / 2;
        data[cy][cx] = 0x000080;
        data[cy - 1][cx] = 0x000080;
        data[cy + 1][cx] = 0x000080;
        data[cy][cx - 1] = 0x000080;
        data[cy][cx + 1] = 0x000080;
    }

    private static void addPortugalEmblem(int[][] data) {
        int color = 0xFFD700;
        data[4][5] = color;
        data[4][6] = color;
        data[5][5] = color;
        data[5][6] = color;
    }

    private static void addSaudiMarks(int[][] data) {
        for (int x = 2; x <= 13; x++) {
            data[2][x] = 0xFFFFFF;
            data[8][x] = 0xFFFFFF;
        }
    }

    private static void addStarOfDavid(int[][] data) {
        int color = 0x0038B8;
        int[][] points = {
                {8, 3}, {8, 4}, {8, 6}, {8, 7},
                {6, 5}, {7, 5}, {9, 5}, {10, 5},
                {9, 4}, {10, 3}, {7, 4}, {6, 3},
                {9, 6}, {10, 7}, {7, 6}, {6, 7},
                {8, 5},
        };
        for (int[] p : points) {
            if (p[0] >= 0 && p[0] < FLAG_WIDTH && p[1] >= 0 && p[1] < FLAG_HEIGHT) {
                data[p[1]][p[0]] = color;
            }
        }
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
