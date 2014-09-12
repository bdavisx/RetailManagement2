package org.loosefx.mvvm.viewmodels;

import javafx.stage.Stage;

public interface InitializableWindowUI<TViewModel> {
    public void initialize( TViewModel viewModel );
}
