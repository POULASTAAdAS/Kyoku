FROM python:3.11-slim

# Set working directory
WORKDIR /app

# Prevent Python from writing pyc files and buffering stdout
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

# Install system dependencies (if needed)
RUN apt-get update && apt-get install -y --no-install-recommends \
    default-libmysqlclient-dev \
    && rm -rf /var/lib/apt/lists/*

# Copy requirements first for better caching
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy Python scripts (one folder per index)
COPY artist/ ./artist/
COPY song/ ./song/

# Default command - can be overridden
CMD ["python", "-c", "print('Run with: python artist/setup_index.py && python artist/import_data.py && python song/setup_index.py && python song/import_data.py')"]
