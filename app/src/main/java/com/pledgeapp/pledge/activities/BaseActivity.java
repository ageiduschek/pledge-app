package com.pledgeapp.pledge.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by ageiduschek on 10/23/15.
 */
public abstract class BaseActivity extends AppCompatActivity {


    protected <T extends Fragment> T attachFragmentToContainer(Class<T> fragmentClassToAdd,
                                                               int containerId,
                                                               Bundle args) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction txn = fragmentManager.beginTransaction();

        for (Class fragmentClass : getFragmentClassesForContainer()) {
            if (!fragmentClass.equals(fragmentClassToAdd)) {
                if (fragmentManager.findFragmentByTag(getFragmentTag(fragmentClass)) != null) {
                    txn.detach(fragmentManager.findFragmentByTag(getFragmentTag(fragmentClass)));
                }
            }
        }


        T attachedFragment;
        String tag = getFragmentTag(fragmentClassToAdd);
        if (fragmentManager.findFragmentByTag(tag) == null) {
            try {
                attachedFragment = fragmentClassToAdd.newInstance();
                attachedFragment.setArguments(args);
                txn.add(containerId /*R.id.flFragmentContainer*/, attachedFragment, tag);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            attachedFragment = (T)fragmentManager.findFragmentByTag(tag);
        }
        txn.attach(attachedFragment);

        txn.commit();

        return attachedFragment;
    }

    protected <T extends Fragment> T getFragment(Class<T> fragmentClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (T) fragmentManager.findFragmentByTag(getFragmentTag(fragmentClass));
    }


    private String getFragmentTag(Class fragmentClass) {
        return this.getClass().getSimpleName() + fragmentClass.getSimpleName();
    }

    protected abstract ArrayList<Class<? extends Fragment>> getFragmentClassesForContainer();
}
