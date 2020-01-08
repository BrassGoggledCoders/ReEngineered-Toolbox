package xyz.brassgoggledcoders.reengineeredtoolbox.component.progressbar;

import com.hrznstudio.titanium.block.tile.progress.PosProgressBar;
import net.minecraft.util.IIntArray;

public class ProgressBarReferenceHolder implements IIntArray {
    private final PosProgressBar posProgressBar;

    public ProgressBarReferenceHolder(PosProgressBar posProgressBar) {
        this.posProgressBar = posProgressBar;
    }

    @Override
    public int get(int index) {
        return index == 0 ? posProgressBar.getProgress() : posProgressBar.getMaxProgress();
    }

    @Override
    public void set(int index, int value) {
        if (index == 0) {
            posProgressBar.setProgress(value);
        } else {
            posProgressBar.setMaxProgress(value);
        }
    }

    @Override
    public int size() {
        return 2;
    }
}
