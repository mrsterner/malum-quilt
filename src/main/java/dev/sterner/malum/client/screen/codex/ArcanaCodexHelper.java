package dev.sterner.malum.client.screen.codex;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sterner.lodestone.setup.LodestoneShaderRegistry;
import dev.sterner.lodestone.systems.easing.Easing;
import dev.sterner.lodestone.systems.recipe.IRecipeComponent;
import dev.sterner.lodestone.systems.rendering.VFXBuilders;
import dev.sterner.lodestone.systems.rendering.shader.ExtendedShader;
import dev.sterner.malum.common.spiritrite.MalumRiteType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class ArcanaCodexHelper {
	public static final VFXBuilders.ScreenVFXBuilder VFX_BUILDER = VFXBuilders.createScreen().setPosTexDefaultFormat();

	public enum BookTheme {
		DEFAULT, EASY_READING
	}

	public static void renderRiteIcon(MalumRiteType rite, DrawContext ctx, boolean corrupted, int x, int y) {
		renderRiteIcon(rite, ctx, corrupted, x, y, 0);
	}

	public static void renderRiteIcon(MalumRiteType rite, DrawContext ctx, boolean corrupted, int x, int y, int z) {
		ExtendedShader shaderInstance = (ExtendedShader) LodestoneShaderRegistry.DISTORTED_TEXTURE.getInstance().get();
		shaderInstance.getUniformOrDefault("YFrequency").set(corrupted ? 5f : 11f);
		shaderInstance.getUniformOrDefault("XFrequency").set(corrupted ? 12f : 17f);
		shaderInstance.getUniformOrDefault("Speed").set(2000f * (corrupted ? -0.75f : 1));
		shaderInstance.getUniformOrDefault("Intensity").set(corrupted ? 14f : 50f);
		Supplier<ShaderProgram> shaderInstanceSupplier = () -> shaderInstance;
		Color color = rite.getEffectSpirit().getColor();

		VFXBuilders.ScreenVFXBuilder builder = VFXBuilders.createScreen()
			.setPosColorTexLightmapDefaultFormat()
			.setShader(shaderInstanceSupplier)
			.setColor(color)
			.setAlpha(0.9f)
			.setZLevel(z)
			.setShader(() -> shaderInstance);

		RenderSystem.enableBlend();
		RenderSystem.disableDepthTest();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		renderTexture(rite.getIcon(), ctx, builder, x, y, 0, 0, 16, 16, 16, 16);
		builder.setAlpha(0.4f);
		renderTexture(rite.getIcon(), ctx, builder, x - 1, y, 0, 0, 16, 16, 16, 16);
		renderTexture(rite.getIcon(), ctx, builder, x + 1, y, 0, 0, 16, 16, 16, 16);
		renderTexture(rite.getIcon(), ctx, builder, x, y - 1, 0, 0, 16, 16, 16, 16);
		if (corrupted) {
			builder.setColor(rite.getEffectSpirit().getEndColor());
		}
		renderTexture(rite.getIcon(), ctx, builder, x, y + 1, 0, 0, 16, 16, 16, 16);
		shaderInstance.setUniformDefaults();
		RenderSystem.enableDepthTest();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
	}

	public static void renderWavyIcon(Identifier location, DrawContext ctx, int x, int y) {
		renderWavyIcon(location, ctx, x, y, 0);
	}

	public static void renderWavyIcon(Identifier location, DrawContext ctx, int x, int y, int z) {
		ExtendedShader shaderInstance = (ExtendedShader) LodestoneShaderRegistry.DISTORTED_TEXTURE.getInstance().get();
		shaderInstance.getUniformOrDefault("YFrequency").set(10f);
		shaderInstance.getUniformOrDefault("XFrequency").set(12f);
		shaderInstance.getUniformOrDefault("Speed").set(1000f);
		shaderInstance.getUniformOrDefault("Intensity").set(50f);
		shaderInstance.getUniformOrDefault("UVCoordinates").set(new Vector4f(0f, 1f, 0f, 1f));
		Supplier<ShaderProgram> shaderInstanceSupplier = () -> shaderInstance;

		VFXBuilders.ScreenVFXBuilder builder = VFXBuilders.createScreen()
			.setPosColorTexLightmapDefaultFormat()
			.setShader(shaderInstanceSupplier)
			.setAlpha(0.7f)
			.setZLevel(z)
			.setShader(() -> shaderInstance);

		RenderSystem.enableBlend();
		RenderSystem.disableDepthTest();
		RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		renderTexture(location, ctx, builder, x, y, 0, 0, 16, 16, 16, 16);
		builder.setAlpha(0.1f);
		renderTexture(location, ctx, builder, x - 1, y, 0, 0, 16, 16, 16, 16);
		renderTexture(location, ctx, builder, x + 1, y, 0, 0, 16, 16, 16, 16);
		renderTexture(location, ctx, builder, x, y - 1, 0, 0, 16, 16, 16, 16);
		renderTexture(location, ctx, builder, x, y + 1, 0, 0, 16, 16, 16, 16);
		shaderInstance.setUniformDefaults();
		RenderSystem.enableDepthTest();
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
	}

	public static void renderTexture(Identifier texture, DrawContext ctx, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
		renderTexture(texture, ctx, VFX_BUILDER, x, y, u, v, width, height, textureWidth, textureHeight);
	}

	public static void renderTexture(Identifier texture, DrawContext ctx, VFXBuilders.ScreenVFXBuilder builder, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
		builder.setPositionWithWidth(x, y, width, height)
			.setShaderTexture(texture)
			.setUVWithWidth(u, v, width, height, textureWidth, textureHeight)
			.draw(ctx.getMatrices());
	}

	public static void renderTransparentTexture(Identifier texture, DrawContext ctx, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
		renderTransparentTexture(texture, ctx, VFX_BUILDER, x, y, u, v, width, height, textureWidth, textureHeight);
	}
	public static void renderTransparentTexture(Identifier texture, DrawContext ctx, VFXBuilders.ScreenVFXBuilder builder, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();
		renderTexture(texture, ctx, builder, x, y, u, v, width, height, textureWidth, textureHeight);
		RenderSystem.disableDepthTest();
		RenderSystem.disableBlend();
	}

	public static void renderComponents(AbstractMalumScreen screen, DrawContext guiGraphics, java.util.List<? extends IRecipeComponent> components, int left, int top, int mouseX, int mouseY, boolean vertical) {
		java.util.List<ItemStack> items = components.stream().map(IRecipeComponent::getStack).collect(Collectors.toList());
		renderItemList(screen, guiGraphics, items, left, top, mouseX, mouseY, vertical).run();
	}

	public static Runnable renderBufferedComponents(AbstractMalumScreen screen, DrawContext guiGraphics, java.util.List<? extends IRecipeComponent> components, int left, int top, int mouseX, int mouseY, boolean vertical) {
		java.util.List<ItemStack> items = components.stream().map(IRecipeComponent::getStack).collect(Collectors.toList());
		return renderItemList(screen, guiGraphics, items, left, top, mouseX, mouseY, vertical);
	}

	public static void renderComponent(AbstractMalumScreen screen, DrawContext guiGraphics, IRecipeComponent component, int posX, int posY, int mouseX, int mouseY) {
		if (component.getStacks().size() == 1) {
			renderItem(screen, guiGraphics, component.getStack(), posX, posY, mouseX, mouseY);
			return;
		}
		int index = (int) (MinecraftClient.getInstance().world.getTime() % (20L * component.getStacks().size()) / 20);
		ItemStack stack = component.getStacks().get(index);
		renderItem(screen, guiGraphics, stack, posX, posY, mouseX, mouseY);
	}

	public static void renderItem(AbstractMalumScreen screen, DrawContext guiGraphics, Ingredient ingredient, int posX, int posY, int mouseX, int mouseY) {
		renderItem(screen, guiGraphics, List.of(ingredient.getMatchingStacks()), posX, posY, mouseX, mouseY);
	}

	public static void renderItem(AbstractMalumScreen screen, DrawContext guiGraphics, java.util.List<ItemStack> stacks, int posX, int posY, int mouseX, int mouseY) {
		if (stacks.size() == 1) {
			renderItem(screen, guiGraphics, stacks.get(0), posX, posY, mouseX, mouseY);
			return;
		}
		int index = (int) (MinecraftClient.getInstance().world.getTime() % (20L * stacks.size()) / 20);
		ItemStack stack = stacks.get(index);
		renderItem(screen, guiGraphics, stack, posX, posY, mouseX, mouseY);
	}

	public static void renderItem(AbstractMalumScreen screen, DrawContext guiGraphics, ItemStack stack, int posX, int posY, int mouseX, int mouseY) {
		MinecraftClient minecraft = MinecraftClient.getInstance();
		TextRenderer font = minecraft.textRenderer;
		guiGraphics.drawItem(stack, posX, posY);
		guiGraphics.drawItemInSlot(font, stack, posX, posY);
		if (screen.isHovering(mouseX, mouseY, posX, posY, 16, 16)) {
			guiGraphics.drawTooltip(font, Screen.getTooltipFromItem(minecraft, stack), mouseX, mouseY);
		}
	}

	public static Runnable renderItemList(AbstractMalumScreen screen, DrawContext ctx, java.util.List<ItemStack> items, int left, int top, int mouseX, int mouseY, boolean vertical) {
		int slots = items.size();
		renderItemFrames(ctx, slots, left, top, vertical);
		return () -> {
			int finalLeft = left;
			int finalTop = top;
			if (vertical) {
				finalTop -= 10 * (slots - 1);
			} else {
				finalLeft -= 10 * (slots - 1);
			}
			for (int i = 0; i < slots; i++) {
				ItemStack stack = items.get(i);
				int offset = i * 20;
				int oLeft = finalLeft + 2 + (vertical ? 0 : offset);
				int oTop = finalTop + 2 + (vertical ? offset : 0);
				renderItem(screen, ctx, stack, oLeft, oTop, mouseX, mouseY);
			}
		};
	}

	public static void renderItemFrames(DrawContext ctx, int slots, int left, int top, boolean vertical) {
		if (vertical) {
			top -= 10 * (slots - 1);
		} else {
			left -= 10 * (slots - 1);
		}
		//item slot
		for (int i = 0; i < slots; i++) {
			int offset = i * 20;
			int oLeft = left + (vertical ? 0 : offset);
			int oTop = top + (vertical ? offset : 0);
			renderTexture(EntryScreen.BOOK_TEXTURE, ctx, oLeft, oTop, 75, 192, 20, 20, 512, 512);

			if (vertical) {
				//bottom fade
				if (slots > 1 && i != slots - 1) {
					renderTexture(EntryScreen.BOOK_TEXTURE, ctx, left + 1, oTop + 19, 75, 213, 18, 2, 512, 512);
				}
				//bottommost fade
				if (i == slots - 1) {
					renderTexture(EntryScreen.BOOK_TEXTURE, ctx, oLeft + 1, oTop + 19, 75, 216, 18, 2, 512, 512);
				}
			} else {
				//bottom fade
				renderTexture(EntryScreen.BOOK_TEXTURE, ctx, oLeft + 1, top + 19, 75, 216, 18, 2, 512, 512);
				if (slots > 1 && i != slots - 1) {
					//side fade
					renderTexture(EntryScreen.BOOK_TEXTURE, ctx, oLeft + 19, top, 96, 192, 2, 20, 512, 512);
				}
			}
		}

		//crown
		int crownLeft = left + 5 + (vertical ? 0 : 10 * (slots - 1));
		renderTexture(EntryScreen.BOOK_TEXTURE, ctx, crownLeft, top - 5, 128, 192, 10, 6, 512, 512);

		//side-bars
		if (vertical) {
			renderTexture(EntryScreen.BOOK_TEXTURE, ctx, left - 4, top - 4, 99, 200, 28, 7, 512, 512);
			renderTexture(EntryScreen.BOOK_TEXTURE, ctx, left - 4, top + 17 + 20 * (slots - 1), 99, 192, 28, 7, 512, 512);
		}
		// top bars
		else {
			renderTexture(EntryScreen.BOOK_TEXTURE, ctx, left - 4, top - 4, 59, 192, 7, 28, 512, 512);
			renderTexture(EntryScreen.BOOK_TEXTURE, ctx, left + 17 + 20 * (slots - 1), top - 4, 67, 192, 7, 28, 512, 512);
		}
	}

	public static void renderWrappingText(DrawContext guiGraphics, String text, int x, int y, int w) {
		var font = MinecraftClient.getInstance().textRenderer;
		text = Text.translatable(text).getString() + "\n";
		java.util.List<String> lines = new ArrayList<>();

		boolean italic = false;
		boolean bold = false;
		boolean strikethrough = false;
		boolean underline = false;
		boolean obfuscated = false;

		StringBuilder line = new StringBuilder();
		StringBuilder word = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			if (chr == ' ' || chr == '\n') {
				if (word.length() > 0) {
					if (font.getWidth(line.toString()) + font.getWidth(word.toString()) > w) {
						line = newLine(lines, italic, bold, strikethrough, underline, obfuscated, line);
					}
					line.append(word).append(' ');
					word = new StringBuilder();
				}

				String noFormatting = Formatting.strip(line.toString());

				if (chr == '\n' && !(noFormatting == null || noFormatting.isEmpty())) {
					line = newLine(lines, italic, bold, strikethrough, underline, obfuscated, line);
				}
			} else if (chr == '$') {
				if (i != text.length() - 1) {
					char peek = text.charAt(i + 1);
					switch (peek) {
						case 'i' -> {
							word.append(Formatting.ITALIC);
							italic = true;
							i++;
						}
						case 'b' -> {
							word.append(Formatting.BOLD);
							bold = true;
							i++;
						}
						case 's' -> {
							word.append(Formatting.STRIKETHROUGH);
							strikethrough = true;
							i++;
						}
						case 'u' -> {
							word.append(Formatting.UNDERLINE);
							underline = true;
							i++;
						}
						case 'k' -> {
							word.append(Formatting.OBFUSCATED);
							obfuscated = true;
							i++;
						}
						default -> word.append(chr);
					}
				} else {
					word.append(chr);
				}
			} else if (chr == '/') {
				if (i != text.length() - 1) {
					char peek = text.charAt(i + 1);
					if (peek == '$') {
						italic = bold = strikethrough = underline = obfuscated = false;
						word.append(Formatting.RESET);
						i++;
					} else
						word.append(chr);
				} else
					word.append(chr);
			} else {
				word.append(chr);
			}
		}

		for (int i = 0; i < lines.size(); i++) {
			String currentLine = lines.get(i);
			renderRawText(guiGraphics, currentLine, x, y + i * (font.fontHeight + 1), getTextGlow(i / 4f));
		}
	}

	private static StringBuilder newLine(List lines, boolean italic, boolean bold, boolean strikethrough, boolean underline, boolean obfuscated, StringBuilder line) {
		lines.add(line.toString());
		line = new StringBuilder();
		if (italic) line.append(Formatting.ITALIC);
		if (bold) line.append(Formatting.BOLD);
		if (strikethrough) line.append(Formatting.STRIKETHROUGH);
		if (underline) line.append(Formatting.UNDERLINE);
		if (obfuscated) line.append(Formatting.OBFUSCATED);
		return line;
	}

	public static void renderText(DrawContext guiGraphics, String text, int x, int y) {
		renderText(guiGraphics, Text.translatable(text), x, y, getTextGlow(0));
	}

	public static void renderText(DrawContext guiGraphics, Text component, int x, int y) {
		String text = component.getString();
		renderRawText(guiGraphics, text, x, y, getTextGlow(0));
	}

	public static void renderText(DrawContext guiGraphics, String text, int x, int y, float glow) {
		renderText(guiGraphics, Text.translatable(text), x, y, glow);
	}

	public static void renderText(DrawContext guiGraphics, Text component, int x, int y, float glow) {
		String text = component.getString();
		renderRawText(guiGraphics, text, x, y, glow);
	}

	private static void renderRawText(DrawContext guiGraphics, String text, int x, int y, float glow) {
		var font = MinecraftClient.getInstance().textRenderer;

		glow = Easing.CUBIC_IN.ease(glow, 0, 1, 1);
		int r = (int) MathHelper.lerp(glow, 163, 227);
		int g = (int) MathHelper.lerp(glow, 44, 39);
		int b = (int) MathHelper.lerp(glow, 191, 228);

		guiGraphics.drawText(font, text, x - 1, y, ColorHelper.Argb.getArgb(96, 255, 210, 243), false);
		guiGraphics.drawText(font, text, x + 1, y, ColorHelper.Argb.getArgb(128, 240, 131, 232), false);
		guiGraphics.drawText(font, text, x, y - 1, ColorHelper.Argb.getArgb(128, 255, 183, 236), false);
		guiGraphics.drawText(font, text, x, y + 1, ColorHelper.Argb.getArgb(96, 236, 110, 226), false);

		guiGraphics.drawText(font, text, x, y, ColorHelper.Argb.getArgb(255, r, g, b), false);
	}

	public static float getTextGlow(float offset) {
		return MathHelper.sin(offset + MinecraftClient.getInstance().player.getWorld().getTime() / 40f) / 2f + 0.5f;
	}

	public static boolean isHovering(double mouseX, double mouseY, int posX, int posY, int width, int height) {
		return mouseX > posX && mouseX < posX + width && mouseY > posY && mouseY < posY + height;
	}
}