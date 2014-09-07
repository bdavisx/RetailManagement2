package org.loosefx.mvvm.viewmodels;

public interface InitializableWithViewModel<TViewModel> {
    public void initialize( TViewModel viewModel );
}
