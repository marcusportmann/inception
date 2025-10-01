#!/usr/bin/env node

const { execSync } = require('child_process');
const readline = require('readline');

console.log('🔍 Running yarn audit for high and critical vulnerabilities...');

try {
	const output = execSync('yarn audit --level high --groups dependencies', {
		encoding: 'utf8',
		stdio: ['pipe', 'pipe', 'pipe'],
	});
	checkVulnerabilities(output);
} catch (error) {
	if (error.stdout) {
		checkVulnerabilities(error.stdout);
	} else {
		console.error('❌ yarn audit failed:', error.message);
		process.exit(1);
	}
}

function checkVulnerabilities(output) {
	const lines = output.split('\n');

	// Look for critical/high vulnerabilities
	let hasCritical = false;
	let hasHigh = false;
	let criticalCount = 0;
	let highCount = 0;
	let moderateCount = 0;
	let lowCount = 0;

	// Scan output for severity indicators
	for (const line of lines) {
		if (line.includes('│ critical      │')) hasCritical = true;
		if (line.includes('│ high          │')) hasHigh = true;
	}

	// Parse summary from the end
	const summary = lines.slice(-5).join('\n');
	const criticalMatch = summary.match(/(\d+) Critical/i);
	const highMatch = summary.match(/(\d+) High/i);
	const moderateMatch = summary.match(/(\d+) Moderate/i);
	const lowMatch = summary.match(/(\d+) Low/i);

	if (criticalMatch) criticalCount = parseInt(criticalMatch[1]);
	if (highMatch) highCount = parseInt(highMatch[1]);
	if (moderateMatch) moderateCount = parseInt(moderateMatch[1]);
	if (lowMatch) lowCount = parseInt(lowMatch[1]);

	const hasCriticalVulns = hasCritical || criticalCount > 0;
	const hasHighVulns = hasHigh || highCount > 0;

	if (hasCriticalVulns || hasHighVulns) {
		console.log('\n' + output);
		console.log('\n❌ HIGH/CRITICAL VULNERABILITIES DETECTED!');

		if (criticalCount > 0) console.log(`   Critical: ${criticalCount}`);
		if (highCount > 0) console.log(`   High: ${highCount}`);
		if (moderateCount > 0) console.log(`   Moderate: ${moderateCount}`);
		if (lowCount > 0) console.log(`   Low: ${lowCount}`);

		console.log('\n🚫 Commit blocked due to security risks');
		console.log('\n💡 RECOMMENDED ACTIONS:');
		console.log('   1. Run `yarn audit --level high` for details');
		console.log('   2. Use `yarn why <package>` to understand dependency chains');
		console.log('   3. Update vulnerable packages if patches are available');
		console.log('   4. Use yarn resolutions for transitive dependencies');
		console.log('   5. Run `yarn audit --fix` if available');
		console.log('');
		console.log('🔧 TO BYPASS THIS CHECK (not recommended):');
		console.log('   Use: `git commit --no-verify`');
		console.log('   This will skip all pre-commit hooks including this security check');
		console.log('');
		console.log('⚠️  Warning: Bypassing security checks should only be done temporarily');
		console.log('   and vulnerabilities should be addressed as soon as possible.');

		if (process.stdin.isTTY) {
			const rl = readline.createInterface({
				input: process.stdin,
				output: process.stdout,
			});

			rl.question('\n⏩ Ignore vulnerabilities and continue? (yes/no): ', (answer) => {
				rl.close();
				if (answer.toLowerCase() === 'yes') {
					console.log('⚠️  Continuing with commit - please fix vulnerabilities soon!');
					process.exit(0);
				} else {
					console.log('✅ Commit aborted. Security first!');
					console.log('💡 Use `git commit --no-verify` to bypass if absolutely necessary.');
					process.exit(1);
				}
			});
		} else {
			console.log('\n🚫 Automated commit rejected due to vulnerabilities');
			console.log('💡 Use `git commit --no-verify` to bypass if absolutely necessary.');
			process.exit(1);
		}
	} else {
		console.log('✅ No high or critical vulnerabilities found');
		if (moderateCount > 0 || lowCount > 0) {
			console.log(`   Moderate: ${moderateCount}, Low: ${lowCount}`);
		}
		process.exit(0);
	}
}
