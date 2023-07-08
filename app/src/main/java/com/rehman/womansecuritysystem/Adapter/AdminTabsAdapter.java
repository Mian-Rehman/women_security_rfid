package com.rehman.womansecuritysystem.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rehman.womansecuritysystem.Driver.AdminAcceptFrag;
import com.rehman.womansecuritysystem.Driver.AdminRejectFrag;
import com.rehman.womansecuritysystem.Driver.AdminReviewFrag;

public class AdminTabsAdapter extends FragmentStateAdapter
{
    public AdminTabsAdapter(@NonNull FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment fragment = null;
        switch (position)
        {
            case 0:
                fragment = new AdminReviewFrag();
                break;

            case 1:
                fragment = new AdminAcceptFrag();
                break;

            case 2:
                fragment = new AdminRejectFrag();
                break;

        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
