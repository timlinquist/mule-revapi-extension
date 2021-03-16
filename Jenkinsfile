def UPSTREAM_PROJECTS_LIST = [ "Mule-runtime/api-annotations/support/1.0.x" ]

Map pipelineParams = [ "upstreamProjects" : UPSTREAM_PROJECTS_LIST.join(','),
                       "mavenSettingsXmlId" : "mule-runtime-maven-settings-MuleSettings",
                       "projectType" : "Runtime"]

runtimeBuild(pipelineParams)
