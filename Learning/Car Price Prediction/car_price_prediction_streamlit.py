import streamlit as st
import pandas as pd
import pickle
from sklearn.preprocessing import OneHotEncoder
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline


# Function to load the saved model
def load_model():
    with open('car_price_model.pkl', 'rb') as f:
        model = pickle.load(f)
    return model


# Function to predict the price
def predict_price(input_data):
    model = load_model()

    # Prepare the input data as a DataFrame
    input_df = pd.DataFrame([input_data])

    # Make the prediction
    predicted_price = model.predict(input_df)

    return predicted_price[0]


# Streamlit app UI
st.title("Car Price Prediction")

st.write("Enter the car details to predict its price:")

# Input fields
brand = st.selectbox('Brand', ['Toyota', 'Ford', 'BMW', 'Honda', 'Hyundai', 'Chevrolet'])
year = st.number_input('Year of Manufacture', min_value=2000, max_value=2023, value=2021)
fuel_type = st.selectbox('Fuel Type', ['Petrol', 'Diesel', 'Electric', 'Hybrid'])
transmission = st.selectbox('Transmission', ['Manual', 'Automatic'])
owner_type = st.selectbox('Owner Type', ['First', 'Second', 'Third'])
km_driven = st.number_input('Kilometers Driven', min_value=0, max_value=200000, value=50000)

# Predict button
if st.button("Predict Price"):
    input_data = {
        'Brand': brand,
        'Year': year,
        'Fuel_Type': fuel_type,
        'Transmission': transmission,
        'Owner_Type': owner_type,
        'Kilometers_Driven': km_driven
    }

    # Get the prediction
    predicted_price = predict_price(input_data)

    st.write(f"The predicted price of the car is: ₹{predicted_price:.2f}")
