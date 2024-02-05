package com.murad.androiddevtask.common

import android.view.LayoutInflater
import android.view.ViewGroup


/**
 * Created on 03 February 2024, 6:30 PM
 * @author Murad Eliyev
 */


typealias Inflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T
