package data;

public class ConsoleLine {

	// opacity
	static final int fade_in_frames = 8;
	static final int full_opacity_frames_normal = 70;
	static final int fade_out_frames = 10;
	// fontsize
	static final int fs_fade_in_frames = 2;
	static final int fs_max_frames = 2;
	static final int fs_fade_out_frames = 2;
	static final int highlighted_fontsize = 23;
	static final int common_fontsize = 20;
//	static final int highlight_frames = 27;
	private String content;
	private int tick_age;
	private int opacity = (int) (Math.random() * 255);
	private double drawYOffset = 0;
	private int fontSize = common_fontsize;
	private int full_opacity_frames;

	public ConsoleLine(String content, int show_factor) {
		this.content = content;
		this.tick_age = 0;
		full_opacity_frames = full_opacity_frames_normal * show_factor;
	}

	public int getTick_age() {
		return tick_age;
	}

	public void tick() {
		this.tick_age++;
		if (tick_age <= fade_in_frames) {
			this.opacity = 255 * tick_age / fade_in_frames;
		} else {
			this.opacity = Math.max(0,
					Math.min(255, -255 * (tick_age - full_opacity_frames - fade_in_frames) / fade_out_frames + 255));
		}

		if (tick_age <= fs_fade_in_frames) {
			this.fontSize = (highlighted_fontsize - common_fontsize) * tick_age / fs_fade_in_frames + common_fontsize; // passt
		} else {
			this.fontSize = Math
					.max(common_fontsize,
							Math.min(highlighted_fontsize,
									(common_fontsize - highlighted_fontsize)
											* (tick_age - fs_max_frames - fs_fade_in_frames) / fs_fade_out_frames
											+ highlighted_fontsize));
//			if (drawYOffset > 0) {
//				fontSize = common_fontsize;
////				opacity = 255;
//			}
//			System.out
//					.println(
//							tick_age + ": "
//									+ ((common_fontsize - highlighted_fontsize)
//											* (tick_age - fs_max_frames - fs_fade_in_frames) / fs_fade_out_frames
//											+ highlighted_fontsize));
//			this.fontSize = (common_fontsize - highlighted_fontsize) * (tick_age - fs_max_frames - fs_fade_in_frames) / fs_fade_out_frames + highlighted_fontsize
		}

		drawYOffset *= 2 / 3.;
	}

	public String getContent() {
		return content;
	}

	public int getOpacity() {
		return opacity;
	}

	public void addOffset(int offset) {
		this.drawYOffset += offset;
//		tick_age += 100;
	}

	public int getOffset() {
		return (int) drawYOffset;
	}

	public int getFontSize() {
		return fontSize;
	}
}
