/*
 * DSL script for geckofx Jenkins jobs
 */
import utilities.common

// Variables
def distros_tobuild = 'precise trusty utopic';
def revision = "\$(echo \${MERCURIAL_REVISION_SHORT} | cut -b 1-6)";

/*
 * Definition of jobs
 */

job {
    common.defaultPackagingJob(delegate, 'geckofx29', 'geckofx29', "", revision, distros_tobuild);

    name 'Geckofx29_NightlyPackaging-Linux-all-master-debug';

    description '''
<p>Nightly builds of the geckofx-29.0 default branch.</p>
<p>The job is created by the DSL plugin from <i>geckofx29-jobs.groovy</i> script.</p>
''';

    triggers {
        // Check once every day for new changes
        // Times are UTC
        scm("H H(4-10) * * *");
    }

    common.hgScm(delegate, 'https://bitbucket.org/geckofx/geckofx-29.0', 'default', 'geckofx29');
}

job {
    common.defaultPackagingJob(delegate, 'geckofx33', 'geckofx33', "", revision, distros_tobuild);

    name 'Geckofx33_NightlyPackaging-Linux-all-master-debug';

    description '''
<p>Nightly builds of the geckofx-33.0 default branch.</p>
<p>The job is created by the DSL plugin from <i>geckofx-jobs.groovy</i> script.</p>
''';

    triggers {
        // Check once every day for new changes
        // Times are UTC
        scm("H H(4-10) * * *");
    }

    common.hgScm(delegate, 'https://bitbucket.org/geckofx/geckofx-33.0', 'default', 'geckofx33');
}

