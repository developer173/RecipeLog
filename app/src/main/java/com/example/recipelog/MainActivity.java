package com.example.recipelog;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.example.recipelog.util.RecipeBackupManager;


public class MainActivity extends AppCompatActivity {

    private Button buttonAddRecipe;
    private Button buttonRecipeList;
    private Button buttonBackup;
    private Button buttonRestore;
    private RecipeBackupManager recipeBackupManager;

    private ActivityResultLauncher<String> backupLauncher;
    private ActivityResultLauncher<String[]> restoreLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAddRecipe = findViewById(R.id.buttonAddRecipe);
        buttonRecipeList = findViewById(R.id.buttonRecipeList);
        buttonBackup = findViewById(R.id.buttonBackup);
        buttonRestore = findViewById(R.id.buttonRestore);
        recipeBackupManager = new RecipeBackupManager(this);

        setupBackupLaunchers();

        buttonAddRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeAddActivity.class);
            startActivity(intent);
        });

        buttonRecipeList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeListActivity.class);
            startActivity(intent);
        });
        buttonBackup.setOnClickListener(v -> {
            String fileName =
                    "recipelog_backup_"
                            + new java.text.SimpleDateFormat(
                            "yyyyMMdd_HHmm",
                            java.util.Locale.JAPAN
                    ).format(new java.util.Date())
                            + ".db";

            backupLauncher.launch(fileName);
        });

        buttonRestore.setOnClickListener(v -> {
            showRestoreConfirmDialog();
        });
    }
    private void setupBackupLaunchers() {

        backupLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.CreateDocument(
                                "application/octet-stream"
                        ),
                        uri -> {
                            if (uri == null) {
                                return;
                            }

                            backupDatabase(uri);
                        }
                );

        restoreLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.OpenDocument(),
                        uri -> {
                            if (uri == null) {
                                return;
                            }

                            restoreDatabase(uri);
                        }
                );
    }
    private void backupDatabase(Uri destinationUri) {
        try {
            recipeBackupManager.backupToUri(destinationUri);

            Toast.makeText(
                    this,
                    "バックアップしました",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "バックアップに失敗しました\n" + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
    private void showRestoreConfirmDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("リストア確認")
                .setMessage(
                        "現在のレシピデータは、選択したバックアップ内容に置き換わります。続けますか？"
                )
                .setNegativeButton("キャンセル", null)
                .setPositiveButton(
                        "続ける",
                        (dialog, which) ->
                                restoreLauncher.launch(
                                        new String[]{
                                                "application/octet-stream",
                                                "application/x-sqlite3",
                                                "*/*"
                                        }
                                )
                )
                .show();
    }
    private void restoreDatabase(Uri sourceUri) {
        try {
            recipeBackupManager.restoreFromUri(sourceUri);

            Toast.makeText(
                    this,
                    "リストアしました。アプリを再起動します",
                    Toast.LENGTH_LONG
            ).show();

            restartApplication();

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "リストアに失敗しました\n" + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }

    }
    private void restartApplication() {
        Intent intent = getPackageManager()
                .getLaunchIntentForPackage(getPackageName());

        if (intent == null) {
            finishAffinity();
            return;
        }

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finishAffinity();
    }

}