/*
 * Copyright (c) 2016 SIL International
 * This software is licensed under the MIT license (http://opensource.org/licenses/MIT)
 */
/*
 * DSL script for LfMerge Jenkins views
 */

/* Definition of views */

class lfMergeViews {
	static void LfMergeViewAll(viewContext) {
		viewContext.with {
			categorizedJobsView('All') {
				description 'All <b>LfMerge</b> jobs'
				filterBuildQueue false
				filterExecutors false

				jobs {
					regex('(^LfMerge.*|^GitHub-(LfMerge|Chorus|FlexBridge).*)')
				}

				columns {
					status()
					weather()
					categorizedJob()
					lastSuccess()
					lastFailure()
					lastDuration()
					buildButton()
				}

				categorizationCriteria {
					regexGroupingRule('^LfMerge.*-any-(default|master)-release$', 'master branch jobs')
					regexGroupingRule('^LfMerge.*-any-live-release$', 'live branch jobs')
					regexGroupingRule('.*(Packaging).*', '')
					regexGroupingRule('^GitHub.*-master-.*', 'Pre-merge builds of GitHub pull requests (master branch)')
					regexGroupingRule('^GitHub.*-lfmerge-.*', 'Pre-merge builds of GitHub pull requests for libraries (lfmerge branch)')
				}
			}
		}
	}

	static void LfMergeViewPackageBuilds(viewContext) {
		viewContext.with {
			listView('Package builds') {
				description 'Package builds of <b>LfMerge</b>'
				filterBuildQueue false
				filterExecutors false

				jobs {
					regex('^LfMerge.*_Packaging-.*')
				}

				columns {
					status()
					weather()
					name()
					lastSuccess()
					lastFailure()
					lastDuration()
					buildButton()
					lastBuildNode()
					lastBuildConsole()
				}
				configure { view ->
					view / columns / 'hudson.plugins.nodenamecolumn.NodeNameColumn'
				}
			}
		}
	}
}
nestedView('LfMerge') {
	configure { view ->
		view / defaultView('All')
	}
	views {
		lfMergeViews.LfMergeViewAll(delegate)
		lfMergeViews.LfMergeViewPackageBuilds(delegate)
	}
}
