#!/bin/bash

# Test script for the refactored shell

echo "Testing refactored shell implementation..."
echo ""

# Build first
echo "Building project..."
mvn clean package -q 2>&1 | grep -E "(BUILD SUCCESS|BUILD FAILURE|ERROR)"

if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

echo "Build successful!"
echo ""

# Create test input file
cat > /tmp/shell_test_input.txt << 'EOF'
echo "Hello World"
echo test
type echo
type type
type ls
type nonexistent
pwd
exit 0
EOF

echo "Running tests..."
echo "================"
echo ""

# Run the shell with test input
./your_program.sh < /tmp/shell_test_input.txt

# Cleanup
rm -f /tmp/shell_test_input.txt

echo ""
echo "================"
echo "Tests completed!"
