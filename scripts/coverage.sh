#!/bin/bash
set -e

COVERAGE_FILE="server/build/reports/kover/report.xml"
WORKTREE_DIR=".main_branch_copy"

extract_coverage() {
    grep -oP 'line-rate="\K[0-9.]+' "$1"
}

# Create worktree for main branch
git fetch origin main
git worktree add $WORKTREE_DIR origin/main

# Run coverage on main
pushd $WORKTREE_DIR
./gradlew server:koverXmlReport --no-daemon
MAIN_COVERAGE=$(extract_coverage "$COVERAGE_FILE")
popd

# Run coverage on current branch
./gradlew server:koverXmlReport --no-daemon
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

# Create Markdown report
echo "### 📊 Test Coverage Comparison" > coverage.md
echo "" >> coverage.md
echo "| Branch         | Coverage |" >> coverage.md
echo "|----------------|----------|" >> coverage.md
echo "| \`main\`         | \`$MAIN_COVERAGE\` |" >> coverage.md
echo "| Current branch | \`$CURRENT_COVERAGE\` |" >> coverage.md
echo "" >> coverage.md
echo "**Change in coverage**: $EMOJI \`$DELTA_FORMATTED\`" >> coverage.md

# Fail if below threshold
THRESHOLD=0.80
if (( $(echo "$CURRENT_COVERAGE < $THRESHOLD" | bc -l) )); then
    echo "❌ Coverage below threshold ($THRESHOLD)"
    exit 1
fi

# Cleanup
git worktree remove $WORKTREE_DIR --force
