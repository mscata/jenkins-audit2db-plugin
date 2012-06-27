USE [Jenkins_Audit]
GO
/****** Object:  Table [dbo].[Jenkins_Build]    Script Date: 06/27/2012 13:03:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Jenkins_Build](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](255) NOT NULL,
	[Full_Name] [varchar](255) NOT NULL,
	[Num] [int] NOT NULL,
	[Start_Time] [datetime] NOT NULL,
	[End_Time] [datetime] NULL,
	[Duration_Secs] [int] NULL,
	[Result] [varchar](255) NULL,
	[User_ID] [varchar](255) NULL,
	[User_Name] [varchar](255) NULL,
	[Server] [varchar](255) NOT NULL,
 CONSTRAINT [PK_Jenkins_Build] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 80) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE NONCLUSTERED INDEX [IX_Jenkins_Build_Duration] ON [dbo].[Jenkins_Build] 
(
	[Duration_Secs] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 80) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_Jenkins_Build_EndTime] ON [dbo].[Jenkins_Build] 
(
	[End_Time] DESC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 80) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_Jenkins_Build_Name] ON [dbo].[Jenkins_Build] 
(
	[Name] ASC,
	[Start_Time] ASC,
	[End_Time] ASC,
	[User_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 80) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_Jenkins_Build_Result] ON [dbo].[Jenkins_Build] 
(
	[Result] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 80) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_Jenkins_Build_Server] ON [dbo].[Jenkins_Build] 
(
	[Server] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 80) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_Jenkins_Build_StartTime] ON [dbo].[Jenkins_Build] 
(
	[Start_Time] DESC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 80) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_Jenkins_Build_UserId] ON [dbo].[Jenkins_Build] 
(
	[User_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 80) ON [PRIMARY]
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Internal identifier.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Short name of the build.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Full name of the build.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build', @level2type=N'COLUMN',@level2name=N'Full_Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'The build number.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build', @level2type=N'COLUMN',@level2name=N'Num'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Timestamp for when the build was started.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build', @level2type=N'COLUMN',@level2name=N'Start_Time'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Timestamp for when the build completed.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build', @level2type=N'COLUMN',@level2name=N'End_Time'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Build duration in seconds.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build', @level2type=N'COLUMN',@level2name=N'Duration_Secs'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'The build result.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build', @level2type=N'COLUMN',@level2name=N'Result'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'The id of the user who started the build (blank for anonymous).' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build', @level2type=N'COLUMN',@level2name=N'User_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'The name of the user who started the build (blank for anonymous).' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build', @level2type=N'COLUMN',@level2name=N'User_Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'The server that is logging this entry.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build', @level2type=N'COLUMN',@level2name=N'Server'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Contains information on Jenkins builds.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build'
GO
/****** Object:  Table [dbo].[Jenkins_Build_Params]    Script Date: 06/27/2012 13:03:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Jenkins_Build_Params](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](255) NOT NULL,
	[Value] [varchar](255) NULL,
	[Build_ID] [int] NOT NULL,
 CONSTRAINT [PK_Jenkins_Build_Params] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 80) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE NONCLUSTERED INDEX [IX_Jenkins_Build_Params_BuildId] ON [dbo].[Jenkins_Build_Params] 
(
	[Build_ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 80) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_Jenkins_Build_Params_Name] ON [dbo].[Jenkins_Build_Params] 
(
	[Name] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON, FILLFACTOR = 80) ON [PRIMARY]
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Internal identifier.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build_Params', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'The parameter name.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build_Params', @level2type=N'COLUMN',@level2name=N'Name'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'The parameter value.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build_Params', @level2type=N'COLUMN',@level2name=N'Value'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'The ID of the build that used this parameter.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build_Params', @level2type=N'COLUMN',@level2name=N'Build_ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Contains information on the parameters used to execute a Jenkins build.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'Jenkins_Build_Params'
GO
/****** Object:  ForeignKey [FK_Jenkins_Build_Params_Jenkins_Build]    Script Date: 06/27/2012 13:03:22 ******/
ALTER TABLE [dbo].[Jenkins_Build_Params]  WITH CHECK ADD  CONSTRAINT [FK_Jenkins_Build_Params_Jenkins_Build] FOREIGN KEY([ID])
REFERENCES [dbo].[Jenkins_Build] ([ID])
GO
ALTER TABLE [dbo].[Jenkins_Build_Params] CHECK CONSTRAINT [FK_Jenkins_Build_Params_Jenkins_Build]
GO
