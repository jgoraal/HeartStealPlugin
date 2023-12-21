package jgor.heartstealplugin.auctions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstantSlots {
    private static final int CATEGORY_SLOT_1 = 12;
    private static final int CATEGORY_SLOT_2 = 14;
    private static final int CATEGORY_SLOT_3 = 20;
    private static final int CATEGORY_SLOT_4 = 22;
    private static final int CATEGORY_SLOT_5 = 24;
    private static final int CATEGORY_SLOT_6 = 30;
    private static final int CATEGORY_SLOT_7 = 32;
    private static final int CATEGORY_SLOT_8 = 40;
    private static final int CATEGORY_SLOT_9 = 43;

    public static final List<Integer> categorySlot = Arrays.asList(CATEGORY_SLOT_1,CATEGORY_SLOT_2,CATEGORY_SLOT_3,CATEGORY_SLOT_4,CATEGORY_SLOT_5,CATEGORY_SLOT_6,CATEGORY_SLOT_7,CATEGORY_SLOT_8,CATEGORY_SLOT_9);

    public static final ArrayList<Integer> itemSlot = AuctionListeners.generateItemSlots();
}
