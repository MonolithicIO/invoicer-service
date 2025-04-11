#!/bin/bash
set -e

COVERAGE_FILE="build/reports/kover/merged/xml/report.xml"

extract_coverage() {
    grep -oP 'line-rate="\K[0-9.]+' "$1"
}

# Run coverage on main branch
git fetch origin main
git checkout origin/main
./gradlew koverMergedReport --no-daemon
MAIN_COVERAGE=$(extract_coverage "$COVERAGE_FILE")

# Back to current branch
git checkout -
./gradlew koverMergedReport --no-daemon
CURRENT_COVERAGE=$(extract_coverage "$COVERAGE_FILE")

# Calculate delta
DELTA=$(echo "$CURRENT_COVERAGE - $MAIN_COVERAGE" | bc -l)
DELTA_FORMATTED=$(printf "%+.2f" "$DELTA")

# Choose emoji
if (( $(echo "$DELTA > 0" | bc -l) )); then
    EMOJI="✅"
elif (( $(echo "$DELTA < 0" | bc -l) )); then
    EMOJI="⚠️"
else
    EMOJI="➖"
fi

# Generate Markdown comment
echo "### 📊 Test Coverage Comparison" > coverage.md
echo "" >> coverage.md
echo "| Branch         | Coverage |" >> coverage.md
echo "|----------------|----------|" >> coverage.md
echo "| \`main\`         | \`$MAIN_COVERAGE\` |" >> coverage.md
echo "| Current branch | \`$CURRENT_COVERAGE\` |" >> coverage.md
echo "" >> coverage.md
echo "**Change in coverage**: $EMOJI \`$DELTA_FORMATTED\`" >> coverage.md

# Fail if current coverage < 0.80
COVERAGE_THRESHOLD=0.80
if (( $(echo "$CURRENT_COVERAGE < $COVERAGE_THRESHOLD" | bc -l) )); then
    echo "❌ Test coverage is below threshold ($COVERAGE_THRESHOLD)."
    exit 1
fi
