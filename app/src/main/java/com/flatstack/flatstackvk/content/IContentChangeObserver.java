package com.flatstack.flatstackvk.content;

import android.content.Context;

public interface IContentChangeObserver {

    void register(final Context context);

    void unregister(final Context context);

}
