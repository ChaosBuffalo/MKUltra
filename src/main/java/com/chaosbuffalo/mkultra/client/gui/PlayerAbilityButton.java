package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.client.gui.lib.*;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerPassiveAbility;
import com.chaosbuffalo.mkultra.network.packets.ActivatePassivePacket;
import com.chaosbuffalo.mkultra.network.packets.ActivateUltimatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.HashSet;

public class PlayerAbilityButton extends MKButton {
    private static int X_POS_TALENT_SLOT_TEX = 22;
    private static int Y_POS_TALENT_SLOT_TEX = 259;
    private static int TALENT_SLOT_WIDTH = 20;
    private static int TALENT_SLOT_HEIGHT = 20;
    private static final int DROP_DOWN_WIDTH = 150;
    private static final int WIDTH = 70;
    private static int ICON_WIDTH = 16;
    private static int ICON_HEIGHT = 16;
    private static final int SLOT_Y_OFFSET = 4;
    private static final int TEXT_OFFSET = 4;
    private static final int SLOT_X_OFFSET = 4;
    public static final int HEIGHT = TALENT_SLOT_HEIGHT + SLOT_Y_OFFSET * 2;
    private static final int DROPDOWN_HEIGHT = 80;
    private static int ICON_X_OFFSET = SLOT_X_OFFSET + (TALENT_SLOT_WIDTH - ICON_WIDTH) / 2;
    private static int ICON_Y_OFFSET = SLOT_Y_OFFSET + (TALENT_SLOT_HEIGHT - ICON_HEIGHT) / 2;

    public enum AbilityType {
        PASSIVE,
        ULTIMATE
    }

    private final AbilityType type;

    private ArrayList<String> tooltip;

    public final PlayerAbility ability;
    public final IPlayerData playerData;
    private MKModal dropdown;
    private boolean isDropdownOpen;
    private int slotIndex;

    public PlayerAbilityButton(PlayerAbility ability, AbilityType type, IPlayerData data, int slotIndex, int x, int y) {
        super(x, y, WIDTH, HEIGHT, "");
        this.ability = ability;
        this.playerData = data;
        this.tooltip = new ArrayList<>();
        isDropdownOpen = false;
        this.type = type;
        this.slotIndex = slotIndex;
        if (ability != null) {
            tooltip.add(ability.getAbilityName());
            tooltip.add(ability.getAbilityDescription());
        }
    }

    @Override
    public void longHoverDraw(Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        if (type == AbilityType.PASSIVE){
            HashSet<PlayerPassiveAbility> learned = playerData.getLearnedPassives();
            if (learned == null || learned.size() == 0) {
                if (getScreen() != null) {
                    getScreen().addHoveringText(new HoveringTextInstruction(
                            I18n.format("mkultra.ui_msg.no_passives"),
                            getParentCoords(new Vec2i(mouseX, mouseY))));
                }
            } else if (ability == null) {
                if (getScreen() != null) {
                    getScreen().addHoveringText(new HoveringTextInstruction(
                            I18n.format("mkultra.ui_msg.learn_passive_prompt"),
                            getParentCoords(new Vec2i(mouseX, mouseY))));
                }
            }
        } else if (type == AbilityType.ULTIMATE){
            HashSet<PlayerAbility> learned = playerData.getLearnedUltimates();
            if (learned == null || learned.size() == 0) {
                if (getScreen() != null) {
                    getScreen().addHoveringText(new HoveringTextInstruction(
                            I18n.format("mkultra.ui_msg.no_ultimates"),
                            getParentCoords(new Vec2i(mouseX, mouseY))));
                }
            } else if (ability == null) {
                if (getScreen() != null) {
                    getScreen().addHoveringText(new HoveringTextInstruction(
                            I18n.format("mkultra.ui_msg.learn_ultimate_prompt"),
                            getParentCoords(new Vec2i(mouseX, mouseY))));
                }
            }
        }
    }

    @Override
    public boolean isInBounds(int x, int y) {
        if (this.skipBoundsCheck) {
            return true;
        }
        return x >= this.getX() + SLOT_X_OFFSET &&
                y >= this.getY() + SLOT_Y_OFFSET &&
                x < this.getX() + SLOT_X_OFFSET + TALENT_SLOT_WIDTH &&
                y < this.getY() + SLOT_Y_OFFSET + TALENT_SLOT_HEIGHT;
    }


    public MKModal getDropdown(int mouseX, int mouseY) {
        MKModal dropdownModal = new MKModal();
        MKScrollView scrollView = new MKScrollView(mouseX - DROP_DOWN_WIDTH / 2, mouseY, DROP_DOWN_WIDTH, DROPDOWN_HEIGHT, true);
        scrollView.setToTop();
        scrollView.setDoScrollX(false);
        dropdownModal.addWidget(scrollView);
        MKStackLayoutVertical layout = new MKStackLayoutVertical(0, 0, DROP_DOWN_WIDTH - 4);
        layout.setPaddingTop(1).setPaddingBot(1).setMarginTop(2).setMarginBot(2);
        layout.doSetWidth(true);
        scrollView.addWidget(layout);
        if (type == AbilityType.PASSIVE){
            HashSet<PlayerPassiveAbility> learned = playerData.getLearnedPassives();
            if (learned == null || learned.size() == 0) {
                return null;
            }
            MKButton emptyButton = new MKButton(I18n.format("mkultra.ui_msg.clear_passive_slot"));
            emptyButton.setPressedCallback((MKButton btn, Integer buttonType) -> {
                if (playerData.canActivatePassiveForSlot(MKURegistry.INVALID_ABILITY, slotIndex)) {
                    MKUltra.packetHandler.sendToServer(new ActivatePassivePacket(MKURegistry.INVALID_ABILITY, slotIndex));
                }
                MKScreen screen = getScreen();
                if (screen != null) {
                    screen.closeModal(this.dropdown);
                }
                return true;
            });
            layout.addWidget(emptyButton);
            for (PlayerPassiveAbility ability : learned) {
                MKButton button = new MKButton(ability.getAbilityName());
                layout.addWidget(button);
                button.setPressedCallback((MKButton btn, Integer buttonType) -> {
                    if (playerData.canActivatePassiveForSlot(ability.getAbilityId(), slotIndex)) {
                        MKUltra.packetHandler.sendToServer(new ActivatePassivePacket(ability.getAbilityId(), slotIndex));
                    }
                    MKScreen screen = getScreen();
                    if (screen != null) {
                        screen.closeModal(this.dropdown);
                    }
                    return true;
                });
            }
        } else if (type == AbilityType.ULTIMATE){
            HashSet<PlayerAbility> learned = playerData.getLearnedUltimates();
            if (learned == null || learned.size() == 0) {
                return null;
            }
            MKButton emptyButton = new MKButton(I18n.format("mkultra.ui_msg.clear_ultimate_slot"));
            emptyButton.setPressedCallback((MKButton btn, Integer buttonType) -> {
                if (playerData.canActivateUltimateForSlot(MKURegistry.INVALID_ABILITY, slotIndex)) {
                    MKUltra.packetHandler.sendToServer(new ActivateUltimatePacket(MKURegistry.INVALID_ABILITY, slotIndex));
                }
                MKScreen screen = getScreen();
                if (screen != null) {
                    screen.closeModal(this.dropdown);
                }
                return true;
            });
            layout.addWidget(emptyButton);
            for (PlayerAbility ability : learned) {
                MKButton button = new MKButton(ability.getAbilityName());
                layout.addWidget(button);
                button.setPressedCallback((MKButton btn, Integer buttonType) -> {
                    if (playerData.canActivateUltimateForSlot(ability.getAbilityId(), slotIndex)) {
                        MKUltra.packetHandler.sendToServer(new ActivateUltimatePacket(ability.getAbilityId(), slotIndex));
                    }
                    MKScreen screen = getScreen();
                    if (screen != null) {
                        screen.closeModal(this.dropdown);
                    }
                    return true;
                });
            }
        }
        scrollView.centerContentX();
        dropdownModal.setOnCloseCallback(() ->
        {
            isDropdownOpen = false;
            dropdown = null;
        });
        return dropdownModal;
    }

    @Override
    public boolean onMousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == UIConstants.MOUSE_BUTTON_RIGHT) {
            MKModal dropdown = getDropdown(mouseX, mouseY);
            if (dropdown != null && getScreen() != null) {
                isDropdownOpen = true;
                this.dropdown = dropdown;
                getScreen().addModal(dropdown);
                return true;
            } else {
                return false;
            }

        } else {
            return super.onMousePressed(minecraft, mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public boolean checkHovered(int mouseX, int mouseY) {
        return isVisible() && isEnabled() && isInBounds(mouseX, mouseY) && !isDropdownOpen;
    }

    @Override
    public void draw(Minecraft minecraft, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        if (this.isVisible()) {
            FontRenderer fontrenderer = minecraft.fontRenderer;
            minecraft.getTextureManager().bindTexture(GuiTextures.CLASS_BACKGROUND_GRAPHIC);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            DrawingUtils.drawTexturedRect(this.getX() + SLOT_X_OFFSET,
                    this.getY() + SLOT_Y_OFFSET,
                    X_POS_TALENT_SLOT_TEX, Y_POS_TALENT_SLOT_TEX,
                    TALENT_SLOT_WIDTH, TALENT_SLOT_HEIGHT,
                    GuiTextures.CLASS_BACKGROUND_WIDTH, GuiTextures.CLASS_BACKGROUND_HEIGHT);
            if (ability != null) {
                ResourceLocation icon = ability.getAbilityIcon();
                minecraft.getTextureManager().bindTexture(icon);
                Gui.drawModalRectWithCustomSizedTexture(this.getX() + ICON_X_OFFSET,
                        this.getY() + ICON_Y_OFFSET,
                        0, 0,
                        ICON_WIDTH, ICON_HEIGHT, ICON_WIDTH, ICON_HEIGHT);
                int textColor = 14737632;
                if (!this.isEnabled()) {
                    textColor = 10526880;
                } else if (isHovered()) {
                    textColor = 16777120;
                }
                int slotOffsetX = SLOT_X_OFFSET + TALENT_SLOT_WIDTH + SLOT_X_OFFSET;
                int fontWidth = getWidth() - slotOffsetX;
                int fontHeight = fontrenderer.getWordWrappedHeight(ability.getAbilityName(), fontWidth);
                int fontYOffset = (getHeight() - fontHeight) / 2 + 1;
                String name;
                if (isHovered()) {
                    if (getScreen() != null) {
                        getScreen().addHoveringText(new HoveringTextInstruction(tooltip,
                                getParentCoords(new Vec2i(mouseX, mouseY))));
                    }
                    name = TextFormatting.DARK_GREEN + ability.getAbilityName();
                } else {
                    name = TextFormatting.BLACK + ability.getAbilityName();
                }
                fontrenderer.drawSplitString(name, this.getX() + slotOffsetX,
                        this.getY() + fontYOffset,
                        fontWidth, textColor);

            }

        }
    }
}