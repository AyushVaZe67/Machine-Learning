import numpy as np
import matplotlib.pyplot as plt

x = np.linspace(0, 10, 100)  # 100 evenly spaced values between 0 and 10
y = np.sin(x)  # Sine function applied to each x value

# Create a plot
plt.figure(figsize=(8, 5))  # Optional: Set the size of the plot
plt.plot(x, y, label='sin(x)', color='blue', linewidth=2)  # Plot y = sin(x)

# Add title and labels
plt.title('Sine Wave')
plt.xlabel('x-axis (0 to 10)')
plt.ylabel('y-axis (sin(x))')

# Add grid
plt.grid(True)

# Add legend
plt.legend()

# Show the plot
plt.show()

