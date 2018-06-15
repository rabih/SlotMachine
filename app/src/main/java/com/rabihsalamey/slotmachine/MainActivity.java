package com.rabihsalamey.slotmachine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "SlotMachine";
    EditText amountBox;
    Button setBankButton;
    Button newGameButton;

    TextView bankBox;
    TextView slotOne;
    TextView slotTwo;
    TextView slotThree;

    Button playButton;

    int bankValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountBox = findViewById(R.id.amount);
        bankBox = findViewById(R.id.bankValue);
        setBankButton = findViewById(R.id.setBank);
        newGameButton = findViewById(R.id.newGame);
        slotOne = findViewById(R.id.slot_first);
        slotTwo = findViewById(R.id.slot_second);
        slotThree = findViewById(R.id.slot_third);
        playButton = findViewById(R.id.play);


        setBankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputVal = amountBox.getText().toString();
                if(inputVal.trim().length() > 0) {    //if nonempty
                    if (Integer.parseInt(inputVal) >= 100 && Integer.parseInt(inputVal) <= 500) {
                        setValue(Integer.parseInt(inputVal));
                        amountBox.setEnabled(false);
                        setBankButton.setEnabled(false);
                        playButton.setEnabled(true);
                        bankBox.setText("$" + Integer.toString(bankValue));
                    } else {
                        //display invalid input message
                        Toast.makeText(getApplicationContext(), "Invalid value of " +
                                Integer.parseInt(inputVal) + ".\nMust be 100 <= value <= 500",
                                Toast.LENGTH_LONG).show();
                        Log.w(TAG, "value of " + inputVal + "not in range");
                    }
                }
                else
                    Log.e(TAG, "amountBox had a null or empty value");
            }
        });

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validAmount())
                    pullLever();
                else
                    resetGame();
            }
        });

        resetGame();
    }

    private boolean validAmount() {
        if(bankValue < 0 || bankValue > 1000)
            return false;
        //display error toast, not enough cash

        return true;
    }

    private void setValue(int bankValue) {
        this.bankValue = bankValue;
    }

    private void resetGame() {
        bankValue = 0;
        bankBox.setText("$" + bankValue);
        amountBox.setText("");
        slotOne.setText("-");
        slotTwo.setText("-");
        slotThree.setText("-");
        amountBox.setEnabled(true);
        setBankButton.setEnabled(true);
        playButton.setEnabled(false);
    }

    private void pullLever() {
        bankValue -= 5;
        int firstSlot = getNumber();
        int secondSlot = getNumber();
        int thirdSlot = getNumber();
        slotOne.setText(firstSlot + "");
        slotTwo.setText(secondSlot + "");
        slotThree.setText(thirdSlot + "");
        calculateResults(firstSlot, secondSlot, thirdSlot);
    }

    private int getNumber() {
        //returns a number between 100 and 900 inclusive (100, 102, ... , 899, 900)
        return (new Random()).nextInt(9-1) + 1;
    }

    private void calculateResults(int firstSlot, int secondSlot, int thirdSlot) {
        if(firstSlot == secondSlot && secondSlot == thirdSlot) { //3 slots
            if(firstSlot < 5)
                bankValue += 40;
            else if (firstSlot >= 5 && firstSlot <= 8)
                bankValue += 100;
            else if(firstSlot == 9)
                bankValue += 1000;
        }
        if(firstSlot == secondSlot || secondSlot == thirdSlot || thirdSlot == firstSlot) //2  matches
            bankValue += 10;

        bankBox.setText("$" + bankValue);

        if(bankValue > 1000)
        {
            Toast.makeText(this, "Winner Winner! Balance of " + bankValue + " . GAME OVER!",
                    Toast.LENGTH_LONG).show();
            resetGame();
            //display you are rich toast
        } else if (bankValue < 0){
            Toast.makeText(this, "Loser Loser! Balance of " + bankValue + " . GAME OVER!",
                    Toast.LENGTH_LONG).show();
            resetGame();
            //display you are broke toast
        }
    }
}
