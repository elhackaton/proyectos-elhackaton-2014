package com.hackaton.carrot;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.hackaton.carrot.db.CarrotDataBase;

public class ProductDataActivity extends CarrotActivity implements
		OnClickListener {
	protected final static String PARAMETER_CONTENTS = "contents";

	private CarrotDataBase mDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productdata);

		Button moreInfoButton = (Button) findViewById(R.id.activity_productdata_button_moreinfo);
		moreInfoButton.setOnClickListener(this);

		String contents = getIntent().getStringExtra(PARAMETER_CONTENTS);

		// Query database.
		mDatabase = new CarrotDataBase(getBaseContext());

		int code = mDatabase.getDangerousFromProduct(contents);
		String image = mDatabase.getImageFromProduct(contents);

		showProductInfo(code, image);
	}

	void showProductInfo(int code, String image) {
		// Sets product image.
		ImageView productImage = (ImageView) findViewById(R.id.activity_productdata_image_product);
		
		int imgRes = R.drawable.no_disponible;
		if (image != null) {
			if (image.compareTo("img01") == 0) {
				imgRes = R.drawable.coca_cola_zero;
			}
			if (image.compareTo("img02") == 0) {
				imgRes = R.drawable.pandorino;
			}
			if (image.compareTo("img03") == 0) {
				imgRes = R.drawable.zumo;
			}			
		}
		
		productImage.setImageResource(imgRes);
		
		// Sets product type.
		int typeRes = -1;

		switch (code) {
		case 0:
			typeRes = R.drawable.nonocivo;
			break;
		case 1:
			typeRes = R.drawable.sospechoso;
			break;
		case 2:
			typeRes = R.drawable.nocivo;
			break;
		}

		ImageView productType = (ImageView) findViewById(R.id.activity_productdata_image_type);
		if (typeRes >= 0) {
			productType.setImageResource(typeRes);
		} else {
			productType.setVisibility(ImageView.INVISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.activity_productdata_button_moreinfo:
			moreInfo();
			break;
		default:
			break;
		}
	}

	public void moreInfo() {
		showMessage("TODO");
	}
}
